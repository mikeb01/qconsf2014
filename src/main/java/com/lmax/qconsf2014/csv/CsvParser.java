package com.lmax.qconsf2014.csv;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CsvParser
{
    public static void parse(
        String documentIdentifier, InputStream reader, CsvHandler handler)
        throws IOException
    {
        CsvStateMachine stateMachine = new CsvStateMachine(handler);
        handler.onStartDocument(documentIdentifier);
        while (stateMachine.onChar(reader.read()))
        {
            // No-op
        }
        handler.onEndDocument();
    }

    public static List<String[]> parseAsList(
        String documentIdentifier, InputStream reader) throws IOException
    {
        final ArrayList<String[]> list = new ArrayList<>();
        final ArrayList<String> line = new ArrayList<>();

        CsvHandler handler = new CsvHandler()
        {
            @Override
            public void onValue(int row, int column, CharSequence value)
            {
                line.add(value.toString());
            }

            @Override
            public void onEndRow(int row)
            {
                list.add(line.toArray(new String[line.size()]));
                line.clear();
            }
        };

        parse(documentIdentifier, reader, handler);

        return list;
    }

    public static Iterable<String[]> parseAsIterable(
        String documentIdentifier, InputStream reader) throws IOException
    {
        return new Iterable<String[]>()
        {
            @Override
            public Iterator<String[]> iterator()
            {
                CsvIterator csvIterator = new CsvIterator(documentIdentifier, reader);
                csvIterator.start();
                return csvIterator;
            }
        };
    }
    
    private static class CsvIterator implements Iterator<String[]>, CsvHandler
    {
        private final InputStream reader;
        private String[] nextLine = null;
        private CsvStateMachine stateMachine;
        private String documentId;
        private List<String> tempLine = new ArrayList<>();
        private boolean lineComplete = false;

        public CsvIterator(String documentId, InputStream reader)
        {
            this.documentId = documentId;
            this.reader = reader;
        }
        
        public void start()
        {
            stateMachine = new CsvStateMachine(this);
            onStartDocument(documentId);
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
        public void onValue(int row, int column, CharSequence value)
        {
            tempLine.add(value.toString());
        }
        
        @Override
        public void onEndRow(int row)
        {
            nextLine = tempLine.toArray(new String[tempLine.size()]);
            tempLine.clear();
            lineComplete = true;
        }
    }
}
