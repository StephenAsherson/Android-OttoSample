/*
 * Copyright (c) 2013 Stephen Asherson
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.stephenasherson.ottosample.contact;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.googlecode.androidannotations.annotations.EActivity;
import com.sa.ottosample.R;

/**
 * Simple main activity class which just creates and
 * displays the initial fragment to create a contact.
 * @author Stephen Asherson.
 */
@EActivity(R.layout.activity_main)
public class MainActivity extends FragmentActivity
{
	/**
	 * Adds the initial fragment if needed.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null)
		{
			addInitialFragments();
		}
	}
	
	/**
	 * Add the initial fragments to the layout
	 */
	private void addInitialFragments()
	{
		CreateContactFragment fragment = CreateContactFragment.newInstance();
		FragmentTransaction t = this.getSupportFragmentManager()
			.beginTransaction();
		t.replace(android.R.id.content, fragment);
		t.commit();
	}
}
