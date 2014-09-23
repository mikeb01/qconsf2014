package com.lmax.qconsf2014.csv;

import java.io.IOException;

class CsvStateMachine
{
    private static final int EOF = -1;
    private static final int CR = '\n';
    private static final int LF = '\r';

    private final StringBuilder buffer = new StringBuilder();
    private final CsvHandler handler;

    private State state = State.START_ROW;
    private int row = 0;
    private int column = 0;

    public CsvStateMachine(CsvHandler handler)
    {
        this.handler = handler;
    }

    public boolean onChar(int c) throws IOException
    {
        boolean result = true;

        switch (state)
        {
            case START_ROW:
                switch (c)
                {
                    case LF:
                    case CR:
                        break;

                    case EOF:
                        result = false;
                        break;

                    default:
                        handler.onStartRow(row);
                        state = State.START_FIELD;
                        result = onChar(c);
                }
                break;

            case START_FIELD:
                switch (c)
                {
                    case '"':
                        state = State.QUOTED_TEXT;
                        break;

                    case ',': // Stay in START_FIELD state
                        notifyValue();
                        break;

                    case LF:
                    case CR:
                        notifyValue();
                        notifyLine();
                        state = State.START_ROW;
                        break;

                    case EOF:
                        notifyValue();
                        notifyLine();
                        result = false;
                        break;

                    default:
                        state = State.TEXT;
                        result = onChar(c);
                }
                break;

            case TEXT:
                switch (c)
                {
                    case ',':
                        notifyValue();
                        state = State.START_FIELD;
                        break;

                    case LF:
                    case CR:
                        notifyValue();
                        notifyLine();
                        state = State.START_ROW;
                        break;

                    case EOF:
                        notifyValue();
                        notifyLine();
                        result = false;
                        break;

                    case '"':
                        throw new IOException("Unexpected '\"'.  Row: " + row + ", Column: " + column);

                    default:
                        buffer.append((char) c);
                }
                break;

            case QUOTED_TEXT:
                switch (c)
                {
                    case '"':
                        state = State.POSSIBLE_DQUOTE;
                        break;

                    case EOF:
                        throw new IOException("Unexpected end of file in quoted text.  Row: " + row + ", Column: " + column);

                    default:
                        buffer.append((char) c);
                }
                break;

            case POSSIBLE_DQUOTE:
                switch (c)
                {
                    case ',':
                        notifyValue();
                        state = State.START_FIELD;
                        break;

                    case '"':
                        buffer.append((char) c);
                        state = State.QUOTED_TEXT;
                        break;

                    case EOF:
                        notifyValue();
                        notifyLine();
                        result = false;
                        break;

                    case LF:
                    case CR:
                        notifyValue();
                        notifyLine();
                        state = State.START_ROW;
                        break;

                    default:
                        throw new IOException("Unexpected value following '\"'.  Row: " + row + ", Column: " + column);
                }
                break;

            default:
                throw new IllegalStateException(state.toString());
        }

        return result;
    }

    private void notifyLine()
    {
        handler.onEndRow(row);
        ++row;
        column = 0;
    }

    private void notifyValue()
    {
        handler.onValue(row, column, buffer);
        buffer.delete(0, buffer.length());
        ++column;
    }
}