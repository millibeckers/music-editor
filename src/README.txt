EMILY J BECKERS & SASHA RUDYAKOV

MUSIC EDITOR, SECOND MOVEMENT

MODIFICATIONS FROM HW05:
Several changes were made from the code that was submitted from HW05. The two model interfaces
were fairly similar, so we just thinned out unnecessary methods from the interfaces to create a
very streamlined interface.
We fixed implementation leaks that were present in HW05, such as references to a concrete Note
class (now an abstract Note class with a package-scoped constructor so that we are the only ones
that can extend it) and returning things in the concrete ArrayList (now just references to the
List interface).
We also made changes to the Note class (and its subclass) to include variables for the velocity
and the instrument. We also made changes so that input comes in the form of a PitchClass enum
and an octave number in order to help sanitize the data.
Lastly, we added support for tempo to the Piece interface.
Other minor changes were made to accommodate the above.


GUI VIEW:
The general design of the concrete GUI view uses Swing to create a frame and a single contentPanel
of the type GUIPanel which subclasses JPanel and draws all of the components of the editor. The
panel is one single class that draws all components such as every beat and the grid on which the
notes are drawn. The GUIViewFrame class, which implements View, does not have a model in its
interface and instead uses render() to pass the model to the GUIPanel, which DOES have the model
as a field and automatically performs a number of drawing operations when the model is passed in.
Render itself has two variations, and the way that this implementation handles render(int beat,
Piece p) is it draws the piece starting from the given note to the end of the piece. Render
(Piece p) simply draws the piece starting at the given note. Because of the nature of these
types of render methods, this particular type of GUi design seemed to make sense, since we need
to be able to draw notes starting at a beat until the end of the song. This is useful for syncing
with the MIDI view and animating the playback of the song.


The GUIViewFrame class is public, implements the View, and extends JFrame. This class is public
because it must be called and constructed to allow users to create and test this type of
concrete View. The class extends JFrame because JFrame is the class within Swing that allows for
the creation of a window that contains JPanel instances which are capable of rendering graphics.

The GUIViewFrame class has several fields that are key in the rendering of appropiately-sized
windows that contain the rendition of a piece of music. Two of these fields are static and final
global constants that are set to two concrete values that may only be changed by the class
editor - CELL_SIZE and MARGIN. MARGIN is a buffer that seperates the measure panel from the
border of the frame, where scales will be drawn. The MARGIN must be even and a multiple of
CELL_SIZE, since it acts as a padding. The CELL_SIZE must be greater than zero, since otherwise
the
cells cannot be drawn. Length and width are fields that determine the size of the actual window
frame, and must be fields of the class since setPreferredSize() cannot take in any arguments.
Instead, setPreferredSize() simply constructs the window to be the size of the length and width
fields.

The constructor for GUIViewFrame does not take in any arguments and only exists to allow the user
to choose when to render the piece. The constructor initializes default values to all of the
fields, such as an empty GUIPanel to be the contentpanel and 100 to be the default length of the
window. If the user uses this constructor without using a render method, the program will display
an empty window.

Render(Piece p) simply calls Render(int beat, Piece p) with the given piece and the starting beat
of the song. This method allows the user to draw the entirety of the piece starting at the first
note in the piece.
Render(int beat, Piece p) initializes the contentpanel using the initialize method within
GUIPanel.
This method will set the variables within the GUIPanel to be the appropriate values contained
within the Piece (the model), including the given start beat at which to begin rendering. Thus,
the start beat cannot be negative.

Both renders automatically call initialize(), which sets the visibility of the frame to be true,
relieving the user of the need to call initialize() himself.

Kill() is a View interface method that simply sets the visibility to false and closes the window
if
it is open. This method is not particularly useful for GUI View, but is needed for the MIDI view
to
stop rendering if the user pauses the song.

The GUIPanel class is package-local and extends JPanel. Visibility is necessary within the scope
of
the package since GUIViewFrame needs to construct instances of GUIPanel. It is not necessary to
make this class public since the only class that uses it is GUIViewFrame within the package.

This class has pointers to the static global variables within GUIViewFrame as fields, since it is
neater to type this.CELL_SIZE or just CELL_SIZE rather than GUIViewFrame.CELL_SIZE.
Piece p is a field, since this class needs to access the model in order to render notes and the
editor in its entirety. HighestPitch and LowestPitch are ints that represent the lowest and
highest
pitches within the piece as MIDI numbers, and are useful for rendering the scale, frame for the
music itself, and the measure bars that divide the piece vertically. The starting note is useful
 as a field because it tells the class from which to beat to begin the rendering.

The initialize() method within this class is strictly called by render within the GUIViewFrame, so
it is package-local to prevent external sources from accessing the method. The method cannot be
private, as it must be called by GUIViewFrame. The method essentially acts as a fake constructor
and sets all of the fields within the class using the given Piece. As soon as these methods are
set, paint() is able to paint all the components of the panel - which it does automatically upon
 initialization.

Paint simply calls a collection of private helper methods within the class that are each
responsible for drawing a particular aspect of the view model. The helper methods all take in the
same Graphics object and draw aspects such as grid lines in the appropriate places with the
appropriate labels. The notes are rendered in a loop that goes through every beat in the song
(starting from the given start beat) and draws all of the notes at that beat.


CONSOLE VIEW:
This one was pretty simple. We had already implemented the console view almost perfectly as a
part of HW05, so all we had to do was massage it to fit into the View interface.


MIDI VIEW:
As this implements the same interface as the GUI view, the reasoning for the different methods
is very similar. The render method that only takes in a piece and not a beat renders the whole
piece straight through, and it works by making calls to the render that takes in a beat and
using calls to Thread.sleep() in order to run it straight through.

There are two constructors for the MIDI view, one doesn't take in anything and this is the
default, it will create a MIDI view that is for output. The other constructor takes in a
Receiver, and this is the one you would call to use the dummy receiver, which we called
Interceptor because it intercepts the messages before they can get to the sound module.