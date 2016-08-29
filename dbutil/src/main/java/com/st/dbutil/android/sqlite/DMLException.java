package com.st.dbutil.android.sqlite;

/**
 * Created by xdata on 7/23/16.
 */
public class DMLException extends Error
{
    public DMLException(String message) {
        super(message);
    }


    public static class DMLStatusException extends DMLException
    {
        public DMLStatusException(String message) {
            super(message);
        }

    }

    public static class DMLInvalidOperationException extends DMLException
    {
        public DMLInvalidOperationException(String message) {
            super(message);
        }

    }

    public static class DMLInvalidColumnException extends DMLException
    {
        public DMLInvalidColumnException(String message) {
            super(message);
        }
    }

    public static class DMLInvalidAliasException extends DMLException
    {
        public DMLInvalidAliasException(String message) {
            super(message);
        }
    }

    public static class DMLColumnNotFound extends DMLException
    {
        public DMLColumnNotFound(String message) {
            super(message);
        }
    }

    public static class DMLInvalidCondicionException extends DMLException
    {
        public DMLInvalidCondicionException(String message) {
            super(message);
        }

    }


    public static class DMLInvalidArgmentException extends DMLException
    {
         public DMLInvalidArgmentException(String message) {
            super(message);
        }
    }

    public static class DMLInvalidTableException extends DMLException
    {

        public DMLInvalidTableException(String message) {
            super(message);
        }
    }
}
