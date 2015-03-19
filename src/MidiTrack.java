import java.util.*;
import java.util.ArrayList;
import java.util.Hashtable;

/*Sacha Perry-Fagant
 * 260571927*/

public class MidiTrack{
    private Hashtable<Character,Integer> noteToPitch;

    private ArrayList<MidiNote> notes;
    private int instrumentId;
    
    // The constructor for this class
    public MidiTrack(int instrumentId){
        notes = new ArrayList<MidiNote>();
        this.instrumentId = instrumentId;
        this.initPitchDictionary();
    }

    // This initialises the noteToPitch dictionary,
    // which will be used by you to convert note letters
    // to pitch numbers
    public void initPitchDictionary(){
        noteToPitch  = new Hashtable<Character, Integer>();
        noteToPitch.put('C', 60);
        noteToPitch.put('D', 62);
        noteToPitch.put('E', 64);
        noteToPitch.put('F', 65);
        noteToPitch.put('G', 67);
        noteToPitch.put('A', 69);
        noteToPitch.put('B', 71);
    }

    // GETTER METHODS
    public ArrayList<MidiNote> getNotes(){
        return notes;
    }
    
    public int getInstrumentId(){
        return instrumentId;
    }
    
    // This method converts notestrings like
    // <<3E3P2E2GP2EPDP8C<8B>
    // to an ArrayList of MidiNote objects 
    // ( the notes attribute of this class )
    //I wrote the following method (MYCODE)
    public void loadNoteString(String notestring){
      // convert the letters in the notestring to upper case
      notestring = notestring.toUpperCase();
      int duration = 0;
      int pitch = 0;
      int octave = 0;
      
      int  length = notestring.length();
      duration = 0;
      for(int i = 0; i < length; i++)
      {
        /*I changed the value in the string to it's ASCII value to make sure it was in the right range.
         * If it's an integer that means that it's a duration so I set duration to that number. 
         * If the duration is greater than 9, the next iteration of the loop will multiply the current duration by 10
         * and add the integer. In the next iteration, it will add this duration to one of the notes then the duration 
         * will be set back to 0. 49 to 57 are the ascii values of 1-9*/
        char a = notestring.charAt(i);
        if((int)a >= 49 && (int)a <= 57)
        {
          if(duration > 0)
          {
            duration= duration*10+Character.getNumericValue(a);
          }
          else
          {
            duration= Character.getNumericValue(a);
          }
        }
        /*If there is an octave change, octave will change by +/- 12. This value will get added to each note afterwards 
         * until the octave changes again. 60 is the ascii value of < and 62 is >, in the imported string, these symbols
         * represent an octave increase or decrease*/
        if((int)a == 60)
        {
          octave= octave - 12;
        }
        
        if((int)a == 62)
        {
          octave= octave + 12;
        }
        
        /*If the value of a ranges from 65 to 71 (A to G) , that means it's a note name. If the duration is set to 0, 
         * then the duration is simply one so I set duration as 1. If duration is not 0, then the duration will 
         * be set what duration is currently then back to 0.*/
        if((int)a >= 65 && (int)a <= 71)
        {
          if(duration != 0)
          {
            int n= noteToPitch.get(a);
            MidiNote sound = new MidiNote(n + octave, duration);
            this.notes.add(sound);
            duration = 0;
          }
          else
          {
            int n= noteToPitch.get(a);
            MidiNote sound = new MidiNote(n + octave, 1);
            this.notes.add(sound);
          }
        }
        /* If a=80 means P, which is a pause. I set the note to silent for this.
         * 21 is just a random number for the pitch since it doesn't matter for a pause.*/
        else if((int)a == 80)
        {
          if(duration != 0)
          {
            MidiNote sound = new MidiNote(21, duration);
            sound.setSilent(true);
            this.notes.add(sound);
            duration = 0;
          }
          else
          {
            MidiNote sound = new MidiNote(21, 1);
            sound.setSilent(true);
            this.notes.add(sound);
          }
        }
        /*If the note is sharp or flat, I created a new note that has the same duration as the last note created
         * and a pitch with an increase or decrease of 1. I then overwrite the old note in the array list.*/
        else if((int)a == 35)
        {
          //sharp (#)
          pitch = pitch + 1;
          int pos= notes.size()-1;
          MidiNote sound= new MidiNote(notes.get(pos).getPitch() + pitch, notes.get(pos).getDuration());
          notes.set(pos, sound);
          pitch = 0;
        }
        
        else if((int)a == 33)
        {
          //flat (!)
          pitch = pitch -1;
          int pos= notes.size()-1;
          MidiNote sound= new MidiNote((notes.get(pos).getPitch()) + pitch, notes.get(pos).getDuration());
          notes.set(pos, sound);
          pitch = 0;
        }
      }
    }
    //End of method

    public void revert(){
        ArrayList<MidiNote> reversedTrack = new ArrayList<MidiNote>();     
        for ( int i = notes.size() - 1; i >= 0; i--){
            MidiNote oldNote = notes.get(i);
            // create a newNote
            MidiNote newNote = new MidiNote(oldNote.getPitch(), oldNote.getDuration());
            
            // check if the note was a pause
            if(oldNote.isSilent()){
                newNote.setSilent(true);
            }
             
            // add the note to the new arraylist
            reversedTrack.add(newNote);
        }
        notes = reversedTrack;
    }

    //To test the code in this file
    public static void main(String[] args){
    	//String notestring = "<<3E3P2E2GP2EPDP8C<8B>3E3P2E2GP2EPDP8C<8B>";
      String notestring = "3C >3C < <3A >3A <3A# >3A #18 P3C >3C < <3A >3A <3A# >3A #18 P";
     // String notestring= "90C!9P9C9C#9P>9C9P9<C9>><<9C>>>C";
      int instrumentId = 0;
        MidiTrack newTrack = new MidiTrack ( instrumentId );
        newTrack . loadNoteString ( notestring );
        MusicInterpreter mi = new MusicInterpreter ();
        mi . setBPM (1200);
        mi . loadSingleTrack ( newTrack );
        mi . play ();
        mi . close (); 
        
    }
}
