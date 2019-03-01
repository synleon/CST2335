package com.example.androidlabs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class ChatRoomActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText editText;

    private MyListAdapter adapter;

    private MyDatabaseOpenHelper dbOpener;

    private SQLiteDatabase db;

    public static final String ACTIVITY_NAME = "CHATROOM_ACTIVITY";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);

        //get a database:
        dbOpener = new MyDatabaseOpenHelper(this);
        db = dbOpener.getWritableDatabase();


        editText = findViewById(R.id.editTextChatMsg);
        ListView listConv = findViewById(R.id.listConversation);
        adapter = new MyListAdapter(this, R.id.listConversation);
        listConv.setAdapter(adapter);

        Button buttonSend = findViewById(R.id.buttonSend);
        buttonSend.setOnClickListener(this);

        Button buttonReceived = findViewById(R.id.buttonReceive);
        buttonReceived.setOnClickListener(this);

        // TODO: open database, read all the messages and insert them into listview control
        //query all the results from the database:
        String[] columns = {MyDatabaseOpenHelper.COL_ID, MyDatabaseOpenHelper.COL_MESSAGE, MyDatabaseOpenHelper.COL_MESSAGE_TYPE};
        Cursor results = db.query(false, MyDatabaseOpenHelper.TABLE_NAME, columns, null, null, null, null, null, null);

        printCursor(results);

        //find the column indices:
        int messageTypeIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_MESSAGE_TYPE);
        int messageIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_MESSAGE);
        int idColIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_ID);

        //iterate over the results, return true if there is a next item:
        results.moveToFirst();
        while(results.moveToNext())
        {
            String message = results.getString(messageIndex);
            String messageType = results.getString(messageTypeIndex);
            long id = results.getLong(idColIndex);

            //add the new Contact to the array list:
            if (messageType.equals("SENT")) {
                adapter.add(new Message(id, message, MessageType.SENT));
            } else {
                adapter.add(new Message(id, message, MessageType.RECEIVED));
            }
        }
    }

    @Override
    public void onClick(View v) {
        String input = editText.getText().toString();

        if (input.length() == 0)
            return;

        // TODO: insert user input into database
        ContentValues newRowValues = new ContentValues();
        newRowValues.put(MyDatabaseOpenHelper.COL_MESSAGE, input);


        long newId = 0;

        switch (v.getId()) {
            case R.id.buttonSend:
                newRowValues.put(MyDatabaseOpenHelper.COL_MESSAGE_TYPE, "SENT");
                newId = db.insert(MyDatabaseOpenHelper.TABLE_NAME, null, newRowValues);
                adapter.add(new Message(newId, input, MessageType.SENT));
                break;
            case R.id.buttonReceive:
                newRowValues.put(MyDatabaseOpenHelper.COL_MESSAGE_TYPE, "RECEIVED");
                newId = db.insert(MyDatabaseOpenHelper.TABLE_NAME, null, newRowValues);
                adapter.add(new Message(newId, input, MessageType.RECEIVED));
                break;
            default:
                break;
        }
        editText.setText("");
    }


    /**
     * MessageType Enum Type
     */
    private enum MessageType { SENT, RECEIVED }

    /**
     * Message representing class
     */
    private class Message {
        private long id;
        private String message;
        private MessageType type;


        public Message(long id, String message, MessageType type) {
            this.id = id;
            this.message = message;
            this.type = type;
        }

        String getMessage() {
            return message;
        }

        MessageType getType() {
            return type;
        }

        public long getId() {
            return id;
        }

        @Override
        public String toString() {
            return "Message{" +
                    "id=" + id +
                    ", message='" + message + '\'' +
                    ", type=" + type +
                    '}';
        }
    }

    /**
     * Customized List Adapter, with built-in container for Message
     */
    private class MyListAdapter extends ArrayAdapter<Message> {
        private LayoutInflater inflater;


        MyListAdapter(Context context, int resource) {
            super(context, resource);
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Message message = getItem(position);

            View view = null;
            TextView textView = null;

            if (message.getType() == MessageType.SENT) {
                view = inflater.inflate(R.layout.chat_message_sent, null);
                textView = view.findViewById(R.id.textViewSent);

            } else if (message.getType() == MessageType.RECEIVED) {
                view = inflater.inflate(R.layout.chat_message_received, null);
                textView = view.findViewById(R.id.textViewReceived);
            }
            textView.setText(message.getMessage());

            return view;
        }
    }

    /**
     * print database information
     * print dateset information
     * @param cursor cursor that containes dataset selected from database
     */
    public void printCursor(Cursor cursor) {

        int columnNumber = cursor.getColumnCount();
        Log.e(ACTIVITY_NAME, "Column number: " + columnNumber);

        for (int i = 0; i < columnNumber; ++i) {
            Log.e(ACTIVITY_NAME, "Column[" + i + "] name:" + cursor.getColumnName(i));
        }

        int rows = cursor.getCount();
        Log.e(ACTIVITY_NAME, "There are " + rows + " rows in cursor");

        while (cursor.moveToNext()) {
            StringBuilder string = new StringBuilder();
            for (int j = 0; j < columnNumber; ++j)
                 string.append(cursor.getString(j) + " ");
            Log.e(ACTIVITY_NAME, string.toString());
        }
    }
}
