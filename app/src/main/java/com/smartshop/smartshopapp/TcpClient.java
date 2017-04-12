package com.smartshop.smartshopapp;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class TcpClient extends Thread
{
    private String serverName;
    private int portNumber;

    private InetAddress address;
    private Socket socket;

    private OnMessageReceived messageListener = null;
    private boolean running = true;
    private boolean connected = false;
    private boolean tryConnect = false;

    private DataOutputStream out;
    private DataInputStream in;

    public TcpClient()
    {

    }

    public TcpClient(String serverName, int portNumber)
    {
        this.serverName = serverName;
        this.portNumber = portNumber;
    }

    public TcpClient(OnMessageReceived listener)
    {
        setOnMessageReceivedListener(listener);
    }

    public void setOnMessageReceivedListener(OnMessageReceived listener)
    {
        messageListener = listener;
    }

    public void sendMessage(String message)
    {
        if (out != null)
        {
            try
            {
                out.writeUTF(message);
                out.flush();
            }
            catch (IOException e)
            {
                // What to do here?
                Log.e("TCP", "Socket Error", e);
            }
        }
    }

    public boolean connect()
    {
        try
        {
            socket = null;
            address = InetAddress.getByName(serverName);

            Log.v("TCP", String.format("Found IP for host %s: %s", serverName, address.toString()));
            Log.v("TCP", String.format("Attempting to connect to %s on port %i", address.toString(),
                                       portNumber));

            socket = new Socket(address, portNumber);
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());

            // Connection successful, we are ready to go
            connected = true;
            return true;
        }
        catch (IOException e)
        {
            Log.e("TCP", "Socket Error", e);

            try
            {
                if (socket != null) socket.close();
            }
            catch (IOException ee)
            {
                Log.e("TCP", "Socket Error Again", e);
            }
        }

        return false;
    }

    public boolean connect(String serverName, int portNumber)
    {
        this.serverName = serverName;
        this.portNumber = portNumber;
        return connect();
    }

    public void stopClient()
    {
        running = false;
    }

    @Override
    public void run()
    {
        try
        {
            while (running)
            {
                if (connected)
                {
                    //in.readInt();
                    /*
                    serverMessage = in.readLine();

                    if (serverMessage != null && mMessageListener != null)
                    {
                        // call the method messageReceived from MyActivity class
                        mMessageListener.messageReceived(serverMessage);
                    }
                    serverMessage = null;*/
                }
            }
        }
        catch (Exception e)
        {
            Log.e("TCP", "Socket Error", e);
        }
    }

    // Declare the interface. The method messageReceived(String message) will
    // must be implemented in the MyActivity
    // class at on asynckTask doInBackground
    public interface OnMessageReceived
    {
        public void messageReceived(String message);
    }
}
