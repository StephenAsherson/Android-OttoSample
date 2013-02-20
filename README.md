Android-OttoSample
==================

[Otto](http://square.github.com/otto/) is an event bus designed to help parts of your Android application
communicate more effectively and in a more decoupled manner.

One particular area where the Otto event bus can be very useful is in the passing of complex data objects between
the [Activity](http://developer.android.com/reference/android/app/Activity.html) and
[Fragment](http://developer.android.com/reference/android/app/Fragment.html) objects in your application. For instance,
when passing a data object between two fragments, traditional methods relied on passing the object via the parent
activity or by using the setTargetFragment()/getTargetFragment methods of the Fragment class; a downside to these
approaches is that it couples your fragments/activities to one another. Whilst one can use Interfaces to alleviate
the coupling, it requires additional boilerplate code and if more than a single object is required one can be faced
with handling the code and coupling of many Interfaces, which is often excessive.

Otto reduces this complexity by allowing the production and subscription of strongly typed objects/events on a single
event bus. This takes away the coupling between Activities and/or Fragments but still allows a given Activity/Fragment
to be strongly tied to the object/event it is interested in.

This project is a simple demonstration of how the Otto event bus can be used to communicate an object between two
fragments. Please note that this project also makes use of [Android Annotations](http://androidannotations.org/) for
object/view depedency injection.

Release Notes
-------------
* v1.0: Initial commit and official 1.0 release

Who Made It?
-----

Stephen Asherson [http://www.stephenasherson.com]
