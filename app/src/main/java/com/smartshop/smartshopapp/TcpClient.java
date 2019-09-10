package com.smartshop.smartshopapp;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.channels.ClosedByInterruptException;

public class TcpClient
{
    private String serverName;
    private int portNumber;

    private InetAddress address;
    private Socket socket;

    private Callback callbackListener = null;
    private boolean running = true;
    private boolean connected = false;
    private boolean tryConnect = false;

    private DataOutputStream out;
    private DataInputStream in;

    private Thread readDataThread;

    public TcpClient()
    {

    }

    public TcpClient(String serverName, int portNumber)
    {
        this.serverName = serverName;
        this.portNumber = portNumber;
    }

    public Callback getCallbackListener()
    {
        return callbackListener;
    }

    public void setCallbackListener(Callback callbackListener)
    {
        this.callbackListener = callbackListener;
    }

    public void send(String message)
    {
        if (connected) new AsyncSend().execute(message);
    }

    public void connect()
    {
        stop();

        new AsyncConnect().execute();
    }

    public void stop()
    {
        // Only stop if we are actually connected
        if (connected)
        {
            // Stop the read data thread
            readDataThread.interrupt();

            // Close the out and in data streams as well as the socket
            try
            {
                out.close();
                in.close();
                socket.close();
            }
            catch (IOException e)
            {
                Log.e("TCP", "Socket Error", e);
            }

            // Set connected to false
            connected = false;
        }
    }

    private class AsyncConnect extends AsyncTask<Void, Void, Boolean>
    {
        protected Boolean doInBackground(Void... args)
        {
            try
            {
                address = InetAddress.getByName(serverName);

                Log.v("TCP",
                      String.format("Found IP for host %s: %s", serverName, address.toString()));
                Log.v("TCP",
                      String.format("Attempting to connect to %s on port %d", address.toString(),
                                    portNumber));

                socket = new Socket(address, portNumber);

                out = new DataOutputStream(socket.getOutputStream());
                in = new DataInputStream(socket.getInputStream());

                // Connection successful, we are ready to go
                return true;
            }
            catch (Exception e)
            {
                Log.e("TCP", "Socket Error", e);

                try
                {
                    if (socket != null) socket.close();
                }
                catch (IOException ee)
                {
                    Log.e("TCP", "Socket Error when closing socket", ee);
                }

                return false;
            }
        }

        protected void onPostExecute(Boolean result)
        {
            connected = result;

            if (callbackListener != null) callbackListener.connect(result);

            // If successfully connected, start the read data thread
            if (connected)
            {
                readDataThread = new Thread(new ReadRunnable());
                readDataThread.start();
            }
        }
    }

    private class AsyncSend extends AsyncTask<String, Void, Boolean>
    {
        protected Boolean doInBackground(String... args)
        {
            try
            {
                out.writeUTF(args[0]);
                out.flush();

                Log.v("TCP", String.format("Sending message: %s", args[0]));

                // Successfully sent, lets go
                return true;
            }
            catch (Exception e)
            {
                Log.e("TCP", "Socket Error", e);

                try
                {
                    if (socket != null) socket.close();
                }
                catch (IOException ee)
                {
                    Log.e("TCP", "Socket Error when closing socket", e);
                }

                return false;
            }
        }

        protected void onPostExecute(Boolean result)
        {
            // There was an error sending a message, stop the client
            if (!result)
            {
                if (callbackListener != null) callbackListener.error();
                stop();
            }
        }
    }

    private class ReadRunnable implements Runnable
    {
        @Override
        public void run()
        {
            try
            {
                // Continue to keep searching for data as long as the thread is not interrupted
                while (!Thread.interrupted())
                {
                    String data = in.readUTF();
                }
            }
            /*catch (InterruptedException e)
            {
                // Do nothing
            }*/
            catch (Exception e)
            {
                // Nothing
            }
        }
    }


    /*public void sendMessage(String message)
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

    public void test()
    {
        tryConnect = true;
    }

    public boolean connect()
    {
        try
        {
            socket = null;
            address = InetAddress.getByName(serverName);

            Log.v("TCP", String.format("Found IP for host %s: %s", serverName, address.toString()));
            Log.v("TCP", String.format("Attempting to connect to %s on port %d", address.toString(),
                                       portNumber));

            socket = new Socket(address, portNumber);
            //socket = new Socket(serverName, portNumber);
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());

            // Connection successful, we are ready to go
            connected = true;
            return true;
        }
        catch (Exception e)
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
                    serverMessage = null;
                }
                else if (tryConnect)
                {
                    // Since we know connected is false, if the try connect is true, try to connect
                    // Report back to connect listener the results
                    final boolean success = connect();

                    new Handler(Looper.getMainLooper()).post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            connectListener.connected(success);
                            Log.d("UI thread", "I am the UI thread");
                        }
                    });

                    tryConnect = false;
                }
            }
        }
        catch (Exception e)
        {
            Log.e("TCP", "Socket Error", e);
        }
    }*/

    // Declare the interface. The method messageReceived(String message) will
    // must be implemented in the MyActivity
    // class at on asynckTask doInBackground
    public interface Callback
    {
        public void messageReceived(String message);

        public void connect(boolean success);

        public void error();

    }
}
