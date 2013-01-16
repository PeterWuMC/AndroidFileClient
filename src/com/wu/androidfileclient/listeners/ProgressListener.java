package com.wu.androidfileclient.listeners;

public interface ProgressListener
{
	void transferred(long num);
	boolean isCancelled();
}