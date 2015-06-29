/*   
 * Copyright 2013-2014 Leonardo Rossetto
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.partner.common.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpRetryException;
import java.net.HttpURLConnection;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.partner.common.constant.HttpConsts;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

/**
 * Created by yuym on 6/8/15.
 */
public class AsyncHttpCallback implements Handler.Callback {


    /** Handler used to pass the messages over the threads */
	private Handler mHandler;

    /** Construct a new instance of AsyncHttpCallback */
	public AsyncHttpCallback() {
		if(Looper.myLooper() != null) mHandler = new Handler(this);
	}

    /**
     * Callback that indicate the request has
     * this method returns the content of the response
     * @param response
     */
	public void onRequestResponse(Response response) { }

    /**
     * Callback that indicate the request has return error
     * @param request
     * @param e
     */
	public void onRequestFailure(Request request, IOException e) { }

    /**
     * Send the success message to the handler
     * @param response
     */
	protected void sendResponseMessage(Response response) {
		sendMessage(obtainMessage(HttpConsts.REQUEST_SUCCESS, response));
	}

    /**
     * Send the fail message to the handler
     * @param request
     * @param e
     */
	protected void sendFailureMessage(Request request, IOException e) {
		sendMessage(obtainMessage(HttpConsts.REQUEST_FAIL, new Object[] {request, e}));
	}

    /**
     * Handle the success message and call the relative callback
     * @param response the request response status code
     * @see #onRequestResponse(Response response)
     */
	protected void handleSuccessMessage(Response response) {
		onRequestResponse(response);
	}

    /**
     * Handle the fail message and call the relative callback
     * @param request
     * @param e
     * @see #onRequestFailure(Request, IOException)
     */
	protected void handleFailMessage(Request request, IOException e) {
		onRequestFailure(request, e);
	}
	
	@Override
	public boolean handleMessage(Message message) {
		switch(message.what) {
			case HttpConsts.REQUEST_SUCCESS:
				Response successResponse = (Response)message.obj;
				handleSuccessMessage(successResponse);
				return true;
			case HttpConsts.REQUEST_FAIL:
				Object[] failResponse = (Object[])message.obj;
				handleFailMessage((Request)failResponse[0], (IOException)failResponse[1]);
				return true;
		}
		return false;
	}

    /**
     * Send a message over the handler,
     * if the handler is null no problems it will recreate it
     * @param message the message for send, can't be null
     */
	protected void sendMessage(Message message) {
		if(mHandler != null) {
			mHandler.sendMessage(message);
		} else {
			handleMessage(message);
		}
	}

    /**
     * Obtain a handler thread message to verify the results
     * this method will always return a valid message
     * @param responseMessage the response message identifier
     * @param response the response object to describle this message
     * @return a valid thread message based on the given parameters
     */
	protected Message obtainMessage(int responseMessage, Object response) {
		Message message = null;
		if(mHandler != null) {
			message = mHandler.obtainMessage(responseMessage, response);
		} else {
			message = Message.obtain();
			message.what = responseMessage;
			message.obj = response;
		}
		return message;
	}
	
}
