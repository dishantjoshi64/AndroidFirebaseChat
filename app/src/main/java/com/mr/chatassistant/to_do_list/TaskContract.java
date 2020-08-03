package com.mr.chatassistant.to_do_list;

import android.provider.BaseColumns;

/**
 * Created by mr on 3/27/2018.
 */

public class TaskContract
{
    public static final String DB_NAME = "tasks.db";
    public static final int DB_VERSION = 1;

    public class TaskEntry implements BaseColumns {
        public static final String TABLE = "tasks";

        public static final String COL_TASK_TITLE = "title";
    }
}

