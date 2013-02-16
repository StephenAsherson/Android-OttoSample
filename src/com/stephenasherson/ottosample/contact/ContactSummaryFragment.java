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
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.res.StringRes;
import com.sa.ottosample.R;
import com.squareup.otto.Subscribe;
import com.stephenasherson.ottosample.events.BusProvider;

/**
 * A fragment that displays the contact information. This class obtains
 * the contact model object by being a subscriber on the otto event
 * bus.
 * 
 * @author Stephen Asherson.
 */
@EFragment
public class ContactSummaryFragment extends Fragment
{
	// Views
	@ViewById
	TextView contactName;

	@ViewById
	TextView contactSurname;

	@ViewById
	TextView contactTelNumber;
	
	// Resource Strings
	@StringRes(R.string.contact_name)
	String contactNamePrefix;
	
	@StringRes(R.string.contact_surname)
	String contactSurnamePrefix;
	
	@StringRes(R.string.contact_telnum)
	String contactTelumPrefix;

	// Injected object
	@Bean
	BusProvider busProvider;

	// the contact being displayed.
	private Contact contact;

	/**
	 * Static factory method. Best practice to use this over a constructor
	 * for fragments.
	 */
	public static ContactSummaryFragment newInstance()
	{
		ContactSummaryFragment_ myFragment = new ContactSummaryFragment_();
		return myFragment;
	}
	
	/**
	 * Retain the instance to hold our data across orientation changes
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	/**
	 * Injection has taken place. Register to retrieve the latest contact
	 * object from the bus. We use an anonymous class to handle the events (see
	 * comments at the bottom of the class). 
	 */
	@AfterInject
	public void postInjection()
	{
		busProvider.getEventBus().register(contactSummaryEventHandler);
	}

	/**
	 * Unregister from the eventbus.
	 */
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		busProvider.getEventBus().unregister(contactSummaryEventHandler);
	}

	/**
	 * The Fragment's UI must be created and returned.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState)
	{
		View fragmentView = inflater.inflate(R.layout.contact_summary_fragment,
			container, false);

		return fragmentView;
	}

	/**
	 * View have been injected. We need to register on the event bus
	 * to receive the latest contact object that is available.
	 */
	@AfterViews
	void updateTextWithDate()
	{
		if (this.contact != null)
		{
			contactName.setText(contactNamePrefix + ": " + contact.getName());
			contactSurname.setText(contactSurnamePrefix + ": " + contact.getSurname());
			contactTelNumber.setText(contactTelumPrefix + ": " + contact.getTelNum());
		}
	}

	/**
	 * Otto has a limitation (as per design) that it will only find
	 * methods on the immediate class type. As a result, if at runtime this instance
	 * actually points to a subclass implementation, the methods registered in this class will
	 * not be found. This immediately becomes a problem when using the AndroidAnnotations
	 * framework as it always produces a subclass of annotated classes.
	 * 
	 * To get around the class hierarchy limitation, one can use a separate anonymous class to
	 * handle the events.
	 */
	private Object contactSummaryEventHandler = new Object()
	{
		/**
		 * Subscription method to receive the latest contact
		 */
		@Subscribe
		public void contactAvailable(ContactAvailableEvent event)
		{
			contact = event.getContact();
		}
	};
}
