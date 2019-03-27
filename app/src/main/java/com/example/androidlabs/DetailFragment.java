package com.example.androidlabs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class DetailFragment extends Fragment {

    private boolean isTablet;
    private Bundle dataFromActivity;

    public void setTablet(boolean tablet) {
        isTablet = tablet;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dataFromActivity = getArguments();
        String message = dataFromActivity.getString(ChatRoomActivity.KEY_MESSAGE);
        long messageId = dataFromActivity.getLong(ChatRoomActivity.KEY_ID);
        String messageType = dataFromActivity.getString(ChatRoomActivity.KEY_TYPE);
        int position = dataFromActivity.getInt(ChatRoomActivity.POSITION);

        // Inflate view
        View result = inflater.inflate(R.layout.fragment_detail, container, false);

        TextView textViewId = result.findViewById(R.id.tv_fragdetail_id);
        textViewId.setText("MessageId=" + Long.valueOf(messageId).toString());

        TextView textViewMsg = result.findViewById(R.id.tv_fragdetail_message);
        textViewMsg.setText("Message=" + message);

        TextView textViewType = result.findViewById(R.id.tv_fragdetail_message_type);
        textViewType.setText("MessageType=" + messageType);

        Button btnDelete = result.findViewById(R.id.btn_fragdetail_delete);
        btnDelete.setOnClickListener( v -> {
            if (isTablet) {
                ChatRoomActivity parentActivity = (ChatRoomActivity)getActivity();
                parentActivity.deleteMessageWithId(messageId, position);
                parentActivity.getSupportFragmentManager().beginTransaction().remove(this).commit();
            }
            else {
                ContainerActivity containerActivity = (ContainerActivity)getActivity();
                Intent backToFragment = new Intent();
                backToFragment.putExtra(ChatRoomActivity.KEY_ID, messageId);
                backToFragment.putExtra(ChatRoomActivity.POSITION, position);

                containerActivity.setResult(Activity.RESULT_OK, backToFragment);
                containerActivity.finish();
            }
        });

        return result;
    }


}
