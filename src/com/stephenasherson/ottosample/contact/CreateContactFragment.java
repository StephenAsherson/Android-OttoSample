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
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.App;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;
import com.sa.ottosample.R;
import com.squareup.otto.Produce;
import com.stephenasherson.ottosample.app.OttoSampleApplication;
import com.stephenasherson.ottosample.events.BusProvider;

/**
 * Fragment class used to create a contact with certain
 * details. The user can select the view summary button
 * to move to a new fragment that displays the contact details.
 * The purpose of this app is to show how the Otto event bus
 * can be used to move objects between fragments. For simplicity,
 * no validation is done on the user input.
 * 
 * @author Stephen Asherson.
 * 
 */
@EFragment
public class CreateContactFragment extends Fragment
{
	// Injection of the application class
	@App
	OttoSampleApplication application;
	
	// A reference to the latest contact we have created.
	private Contact latestContact;

	// Views
	@ViewById
	EditText contactName;

	@ViewById
	EditText contactSurname;

	@ViewById
	EditText contactTelNumber;

	// Injected object
	@Bean
	BusProvider busProvider;

	/**
	 * Static factory method. Best practice to use this over a constructor
	 * for fragments.
	 */
	public static CreateContactFragment newInstance()
	{
		CreateContactFragment myFragment = new CreateContactFragment_();
		return myFragment;
	}

	/**
	 * Retain the instance to hold our data across orientation changes
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		// check if there is a cached contact
		latestContact = application.getLatestContact();
	}
	
	/**
	 * Injection has taken place. Register to retrieve the latest contact
	 * object from the bus. We use an anonymous class to handle the events (see
	 * comments at the bottom of the class). 
	 */
	@AfterInject
	public void postInjection()
	{
		busProvider.getEventBus().register(createContactEventHandler);
	}

	/**
	 * Unregister from the eventbus.
	 */
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		busProvider.getEventBus().unregister(createContactEventHandler);
	}

	/**
	 * The Fragment's UI must be created and returned.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState)
	{
		View fragmentView = inflater.inflate(R.layout.create_contact_fragment,
			container, false);

		return fragmentView;
	}

	/**
	 * Handle the click of the view summary button
	 */
	@Click(R.id.contactViewSummary)
	void viewSummarySelected()
	{
		// first create the contact object
		latestContact = new Contact(contactName.getText().toString(),
			contactSurname.getText().toString(), contactTelNumber.getText()
				.toString());
		
		// Cache the contact in the event of orientation change.
		// For demo purposes, this is done in a crude way by storing in the application class
		application.setLatestContact(latestContact);
		
		// show the about page
		ContactSummaryFragment fragment = ContactSummaryFragment.newInstance();
		FragmentTransaction t = getActivity().getSupportFragmentManager()
			.beginTransaction();
		t.setCustomAnimations(android.R.anim.slide_in_left,
			android.R.anim.fade_out, android.R.anim.fade_in,
			android.R.anim.slide_out_right);
		t.replace(android.R.id.content, fragment);
		t.addToBackStack(null);
		t.commit();
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
	private Object createContactEventHandler = new Object()
	{
		/**
		 * This class is a producer of Contact objects on the eventbus.
		 */
		@Produce
		public ContactAvailableEvent produceContact()
		{
			return new ContactAvailableEvent(latestContact);
		}
	};
}
