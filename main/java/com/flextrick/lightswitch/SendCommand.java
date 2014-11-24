package com.flextrick.lightswitch;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.ByteArrayOutputStream;
import java.util.Properties;

/**
 * Created by Daniel Huber on 24.11.2014.
 */
public class SendCommands extends AsyncTask<String, Void, String> {

    ProgressDialog progressDialog;
    private Context mContext;

    public SendCommands(Context context){
        mContext = context;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setTitle("Uno momento..");
        progressDialog.show();
    }
    @Override
    protected String doInBackground(String... params) {
        JSch jsch = new JSch();
        Session session = null;
        try {
            session = jsch.getSession("user", "server", "port");
        } catch (JSchException e) {
            e.printStackTrace();
        }
        if (session != null) {
            session.setPassword("PWD");
            Properties prop = new Properties();
            prop.put("StrictHostKeyChecking", "no");
            session.setConfig(prop);

            try {
                session.connect();
            } catch (JSchException e) {
                e.printStackTrace();
            }

            // SSH Channel
            ChannelExec channelssh = null;
            try {
                channelssh = (ChannelExec)
                        session.openChannel("exec");
            } catch (JSchException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            channelssh.setOutputStream(baos);

            // Execute command
            for (int i = 0; i < params.length; i++) {
                channelssh.setCommand(params[i]);
            }
            try {
                channelssh.connect();
            } catch (JSchException e) {
                e.printStackTrace();
            }
            channelssh.disconnect();

        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        if(progressDialog!=null)
            progressDialog.dismiss();
    }



    @Override
    protected void onProgressUpdate(Void... values) {}
}
