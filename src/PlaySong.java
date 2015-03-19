import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
/*Sacha Perry-Fagant
 * 260571927*/

//I wrote the following method (MYCODE)
public class PlaySong{
    public static void main( String[] args){
        MusicInterpreter myMusicPlayer = new MusicInterpreter();
        System.out.println(myMusicPlayer.availableInstruments());
        String song_file_path = "./data/07.txt ";
        Song mySong = new Song ();
        try
        {
        mySong.loadFromFile(song_file_path);
        }
        catch (Exception e)
        {
          System.out.println("File not found.");
        }
        MusicInterpreter mi = new MusicInterpreter ();
        mi . loadSong ( mySong );
        mi . play ();
        mi . close ();
        
        /*The following is the song that I wrote, I chose Let it Snow*/
        MusicInterpreter myMusicPlayera = new MusicInterpreter();
        System.out.println(myMusicPlayera.availableInstruments());
        String song_file_patha = "./data/LetItSnow.txt ";
        Song mySonga = new Song ();
        try
        {
        mySonga.loadFromFile(song_file_patha);
        }
        catch (Exception e)
        {
          System.out.println("File not found.");
        }
        MusicInterpreter mia = new MusicInterpreter ();
        mia . loadSong ( mySonga );
        mia . play ();
        mia . close ();
    }
}

/*
{

The following are the instructions provided for this assignment.

Playing music from notestrings
In this part, you will write code to make your computer play simple melodies from a note string specification. Your task is to transform that given string into instances of notes. These instances will
be used to produce sounds out of your computer speakers using the provided interpreter. You will need
the following _les for this question: MusicInterpreter.java, MidiTrack.java and MidiNote.java.
A notestring3 is a sequence of characters which encodes the order and timing of musical notes in a melody.
This is an example of a notestring: \<<3E3P2E2GP2EPDP8C<8B>". The letters `A', `B', `C', `D', `E',
`F', `G' correspond to the notes on a musical scale, each one having a corresponding pitch. The letter
`P' represents a pause, or the absence of a note. The numbers represent the duration, measured in beats,
of the note or pause they precede. The symbols `>' and `<' will change how high or low a particular
note will sound like.
Here's an overview of the purpose of each _le (also detailed in Figure 2, at the end of this document).
The MusicInterpreter class takes care of all the sound generation. It uses a synthesizer to generate
sounds, and a sequencer to determine the order and timing of sounds. You don't have to worry about
implementing this, it is already implemented for you. MidiTrack class stores all the information from a
notestring: it has an instrumentId, to determine which instrument sound should be used, and a list of
notes, implemented as an ArrayList of MidiNote objects. A MidiNote object stores two properties of
a single note: its pitch, its duration.
You have to implement the loadNoteString method of the MidiTrack.java _le. This method receives
a String variable containing a notestring as input. Your method should process the notestring character
by character, creating MidiNote objects with the appropriate pitch and duration. The items a. to e.
describe this process in more detail.
In order to test your notestring parsing, use the loadSingleTrack and the play methods of the
MusicInterpreter class, in a similar way to the following code sample. Hint: place this code in a
main method and try running an instance of MidiTrack.java _le.
3This is inspired by http://www.danielzingaro.com/sound_proc/assignment.html.

// Build the MidiTrack object
String notestring = "3C >3C <<3A >3A <3A#>3A #18 P3C >3C <<3A >3A <3A#>3A #18 P";
int instrumentId = 0;
MidiTrack newTrack = new MidiTrack ( instrumentId );
newTrack . loadNoteString ( notestring );
// Build a MusicInterpreter and set a playing speed
MusicInterpreter mi = new MusicInterpreter ();
mi. setBPM (1200);
// Load the track and play it
mi. loadSingleTrack ( newTrack );
mi. play ();
// close the player so that your program terminates
mi. close ();
Make sure you test each item before moving to the next one. Hint: To process a notestring character
by character you need a loop and conditional statements for each of the following cases.
(a) Creating MidiNote objects: When processing a notestring, you must create a new instance of
MidiNote class each time you _nd any of the following characters `A', `B', `C', `D', `E', `F', `G'.
Every MidiNote that you create should be added to the notes attribute of the MidiTrack class.
Since notes is an ArrayList, use the add method to do this.
The constructor of a MidiNote object requires a pitch value, and a duration. To determine the
pitch value, you should use the noteToPitch Hashtable of the MidiTrack class. For example,
calling noteToPitch.get(`C') will return the value of 60. By default, the duration should be 1,
corresponding to one beat.
For instance, if you pass the notestring ``CDEFGAB'' to the loadNoteString method, your code
should add 7 MidiNote objects to the notes ArrayList of the MidiTrack object, with pitches
corresponding to 60, 62, 64, 65, 67, 69, and 71; and all of them with a duration of 1 beat.
(b) Pauses between notes:
Your code must support reading the character `P' as a silence instead of playing a note. For
example, playing the MidiTrack corresponding to the notestring ``CCCC'' should sound as the
same note played 4 times, while ``CPCPCPCP'' should sound as the same note played 4 times, but
with a pause after each note. The duration of a pause is 1 beat. Use the setSilent method of the
MidiNote class to mark a note as a pause, and add this silent MidiNote each time you encounter a
`P'.
As an example, if you pass the notestring ``CPC'' to the loadNoteString method, your code
should add 3 MidiNote objects to the notes ArrayList of the MidiTrack object all of them with
a duration of 1 beat. The _rst and third MidiNote will have pitch equal to 60, and the silent
attribute equal to false. The second one can have any pitch value, as long as its silent attribute
is equal to true.
(c) Note durations:
This reader supports notes of di_erent durations. When a number N appears before a note or
pause, then it means that the note or pause should have a duration of N beats. For example, the
following ``CCCC'' should sound like 4 separate notes, where are ``4C'' should sound like a single
longer note. On the other hand ``C2C4C8C'' should sound like 4 notes of increasing duration. You
code should support a duration N that spans over multiple digits, e.g. ``18C'' should sound like
a note that last for 18 beats.
(d) Octaves:
In the notestring notation, the character `>' stands for increasing the pitch of all subsequent
notes by 1 octave, while the `<' character stands for decreasing the pitch of all subsequent notes
by 1 octave. Your code must support reading these two characters.

When you increase/decrease the pitch of a note by one octave, it sounds like the same note but
with a higher/lower pitch. With the MidiNote class, going an octave up corresponds to increasing
the pitch by 12, while going an octave down corresponds to decreasing the pitch by 12. Thus, every
time the reader encounters the `>' character, you should add 12 to the pitch of all subsequent
notes. Conversely, when your code processes a `<' character, you should subtract 12 from the pitch
of all subsequent notes.
For example, in the notestring ``CDE>CDE<CDE'' the _rst three notes will be the same as the last
three, but the middle three will have a higher pitch. The notestring ``4E>4E>4E>4E<<<4E'', will
sound like 4 E notes with increasing pitch, and a _fth E note with the same pitch as the _rst one.
Take a look at at http://newt.phys.unsw.edu.au/jw/notes.html to see the pitch numbers of
notes at di_erent octaves.
(e) Flat and Sharp notes:
The _nal requirement are the sharp modi_er, which increases the pitch by 1, and at modi_er,
which decreases the pitch by 1. We will use the `#' symbol for sharp notes and the `!' symbol
for at notes. This will work di_erently from the octave change. The at and sharp symbols apply
only to the single note that is immediately before the at/sharp symbol. For example, ``C#'' is a
C sharp note, ``B!" is a B at note and ``FF#'' corresponds to a regular F note followed by an F
sharp. The pitch values of the previous examples are 61, 70, 65 and 66, respectively.
Visit this webpage http://cs.mcgill.ca/~cs202/2014-09/web/a5/notestrings.html for a list of
example notestring and sample sounds, to compare the result from your code.

Loading multiple notestrings from a text _le.
For this part, you will need the _le Song.java, in addition to the _les from Question 3. We provided you
some text _les inside the data folder to test your code. An object of the Song class contains information
about the speed at which the song will be played, the instrument sounds that will be available for the
MusicInterpreter synthesizer, and a list of tracks to be played.
The speed of the Song is stored in its myBeatsPerMinute attribute. The attribute mySoundbank, de_nes
a location of a soundbank _le, which contains a collection sounds for the synthesizer. The attribute
myTracks is an ArrayList of MidiTrack objects. This will allow us to play polyphonic tunes, by playing
multiple notestrings at the same time.
You will write code to open a Song _le similar to the one shown in Figure 1, load its contents to create
an object of Song class, and play it using the MusicInterpreter class. The following items describe
what you need to do in more detail.
(a) Opening a Song _le:
In the Song.java _le, implement the loadSongFromFile method. This method receives a _le path
as an input, which corresponds to the location of a Song _le. Each line in a Song _le consists of a
property name followed by a property value, separated by the ``='' symbol. Figure 1 depicts an
example of one such Song _le.
name = SimpleTune
bpm = 100
soundbank = ./ data / Famicom .sf2
instrument = 0
track = CDEFGAB
instrument = 1
track = GABCDEF

Inside a Song _le, we define the following properties of a Song object:
_ name: The song name
_ bpm: The speed of the song in beats per minute, this value should be interpreted as an integer
number
_ soundbank: The path to a _le containing a collection of sounds that will be available to the
MusicInterpreter class, this value should be interpreted as a String
_ track: A sequence of symbols corresponding to a notestring, This value should be interpreted
as a String.
_ instrument: When this element appears before a track, it de_nes the instrument from the
soundbank that will be used for playing the track, this value should be interpreted as an
integer number
You will have to write the code that opens the _le at the speci_ed path, and use a BufferedReader
to process it line by line. For determining the property name and value of each line, you might
want to use some of the following methods of the String class: replace, startsWith, split, and
trim.
The lines specifying the name, bpm and soundbank properties must be used to set the myName,
myBeatsPerMinute and mySoundbank attributes of the Song class. The lines specifying the track
and instrument properties must be used to construct a new MidiTrack object. Each MidiTrack
object that you create has to be added to the myTracks ArrayList of the Song class.
If the _le that your method is trying to open does not exist, or if it cannot be opened, the code will
throw an IOException. You must NOT catch the exception, but pass it to the caller. To do this,
add throws IOException to the declaration of your method. This will postpone the error handling
to the next part, where the loadSongFromFile method will be used.
(b) Creating and playing a Song object:
In the PlaySong.java, modify the main method, so that it creates a Song object, and calls the
loadSongFromFile with a input _lename String (song file path in the example below).
After the _le has been loaded, call the loadSong and play methods of a MusicInterpreter object.
Your main method should look similar to the following code snippet
String song_file_path = "./ data /07. txt ";
Song mySong = new Song ();
*
* call loadSongFromFile , handling the Exeptions correctly
*
MusicInterpreter mi = new MusicInterpreter ();
// Load the Song and play it
mi. loadSong ( mySong );
mi. play ();
// close the player so that your program terminates
mi. close ();
Remember to handle the exceptions that might arise when opening a _le; e.g. exceptions
of the IOException type. Print a meaningful message when you catch an Exception; i.e. something
short that tells the user what the error was ( ``Could not open the file'', ``The file does
not exist'', etc.).
If all goes well, when you execute the main method of an instance of type PlaySong class with
song file path ``./data/07.txt'', you should be listening to something similar to the Mario
Underworld Theme 4.
4http://youtu.be/c0SuIMUoShI

Create a new Song _le that reproduces a simpli_cation some tune that you like. It should have at
least 10 notes and 2 tracks. In the name property of the Song _le, you should put an URL pointing
to a Youtube video of the tune that you are reproducing. We will use your own code to test your
creation.

Writing a Reverse Song File
In the previous part you wrote a class that would open a _le for reading, and build a Song object using
the properties speci_ed inside a Song _le. In this part, you will write code that works in the opposite
direction, you will create a class named SongWriter, that will take Song objects, and convert them into
text _les with the correct format. To do this, you will have to complete the following tasks inside the
SongWriter.java _le.
a. The noteToString method:
This part consists of converting a single note into its notestring representation. Implement the
noteToString method, which takes as input a MidiNote object and returns a String representation
of the note in the notestring format of the previous question. You should use the pitchToNote
Hashtable which translates pitch numbers to note names.
For example, if the MidiNote has a duration of 12 beats and a pitch equal to 65, then the method
returns the string ``12F''. If the isSilent() method of such object returns true, then the method
returns ``12P'' instead.
This method ignores octaves; e.g. if the MidiNote has a duration of 12 beats a pitch equal to 89
(corresponding to an F note, two octaves up) the method returns ``12F''. Use the getOctave()
method of the MidiNote class, to get the number of octaves that you need to add or subtract to
_nd the corresponding note symbol in the pitchToNote Hashtable. You don't have to worry about
a note being sharp or at, since the pitchToNote Hashtable has entries for those pitch values.
b. The trackToString method:
Implement the trackToString method, which takes a MidiTrack object as input, and returns a
valid notestring representation of the MidiTrack. In this method, use the noteToString method
to get the notestring representation of each MidiNote in the track. You should handle octave
changes by checking the octave di_erence of consecutive MidiNote objects in the MidiTrack. Add
the correct number of octave change symbols, `<' or `>', depending of the octave di_erence.
For example, if a note with pitch equal to 48 ( corresponding to a C, with octave = -1 ) is followed
by a note with pitch equal to 89 ( corresponding to an F note, with octave = 2 ), then the resulting
notestring should look like ``...C>>>F...''. Use the getOctave() method of the MidiNote class,
to compute this di_erence.
You can test this method by loading a Song _le using the code from the Question 3, and checking
if the strings returned by the trackToString method are equivalent to those in the Song _le.
c. The writeToFile method:
Implement the writeToFile method. This method receives as input a String filename and a Song
object to write. This method will open a _le with the given _lename inside the data folder, and
write its content using the format of the Song _les of Question 3.
Use the Filewriter and BufferedWriter to open and write the contents of the _le. Your code
should write one line for each of the myName, myBeatsPerMinute, and mySoundbank attributes of
the Song object. These lines correspond to the name, bpm, and soundbank properties of the
Song _le format.
Your code should write a pair of lines for the instrumentId and the notestring representation
of each MidiTrack in the myTracks attribute of the Song object. Use trackToString to get the

notestring representation of each track. These lines correspond to the instrument and track
properties of the Song _le format.
If the path that your method is trying to open is not valid or if the _le cannot be opened, the code
throws an IOException. You should NOT catch the exception, but pass it to the caller. To do
this, add throws IOException to the declaration of your method.
d. The main method in SongWriter.java:
Inside the Song class, we provided you with a revert method. This method reverses the notes
order of every track, by calling the revert method of each MidiNote class.5 What you need to do
is to write the main method in the SongWriter.java _le, so that it performs the following steps:
_ Load a Song _le into a Song object.
_ Call the revert method of the Song object.
_ Use the writeToFile method of a SongWriter object to write the new Song _le. The name
of the new _le should be the name of the Song followed by `` reverse''. For example, if you
load the _le ``07.txt'', which has the line name = underworld, then the name of the new
_le will be ``underworld reverse.txt''.
Remember to handle the exceptions that might arise when opening a _le; e.g. exceptions
of the IOException type. Print a meaningful message when you catch an Exception. If your code
generates a correct Song _le, then this _le will be playable by the code you wrote on Question 3.
Try it out with the ``07.txt'' _le from the data folder. The resulting sound should be similar to
the one on this video http://youtu.be/lOT19KIwN_o (the Mario Underworld theme in reverse).

}
 */