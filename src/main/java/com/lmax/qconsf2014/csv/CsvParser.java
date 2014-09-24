package com.lmax.qconsf2014.csv;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CsvParser
{
    public static void parse(InputStream reader, CsvObservable<CharSequence> handler) throws IOException
    {
        CsvStateMachine stateMachine = new CsvStateMachine(handler);
        while (stateMachine.onChar(reader.read()))
        {
            // No-op
        }
        handler.onComplete();
    }

    public static Iterable<String[]> parseAsIterable(InputStream reader) throws IOException
    {
        return new Iterable<String[]>()
        {
            @Override
            public Iterator<String[]> iterator()
            {
                CsvIterator csvIterator = new CsvIterator(reader);
                csvIterator.start();
                return csvIterator;
            }
        };
    }
    
    private static class CsvIterator implements Iterator<String[]>, CsvObservable<CharSequence>
    {
        private final InputStream reader;
        private String[] nextLine = null;
        private CsvStateMachine stateMachine;
        private List<String> tempLine = new ArrayList<>();
        private boolean lineComplete = false;

        public CsvIterator(InputStream reader)
        {
            this.reader = reader;
        }
        
        public void start()
        {
            stateMachine = new CsvStateMachine(this);
            readNextLine();
        }

        private void readNextLine()
        {
            nextLine = null;
            
            try
            {
                while (stateMachine.onChar(reader.read()))
                {
                    if (lineComplete)
                    {
                        lineComplete = false;
                        break;
                    }
                }
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }

        @Override
        public boolean hasNext()
        {
            return nextLine != null;
        }

        @Override
        public String[] next()
        {
            String[] result = nextLine;
            readNextLine();
            
            return result;
        }
        
        @Override
        public void onEvent(int row, int column, CharSequence value, boolean endOfLine)
        {
            tempLine.add(value.toString());
            
            if (endOfLine)
            {
                nextLine = tempLine.toArray(new String[tempLine.size()]);
                tempLine.clear();
                lineComplete = true;
            }
        }
    }
}
