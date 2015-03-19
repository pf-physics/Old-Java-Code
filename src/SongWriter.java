import java.util.ArrayList;
import java.util.Hashtable;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/*Sacha Perry-Fagant
 * 260571927*/

public class SongWriter{
  private Hashtable<Integer,String> pitchToNote;
  
  // The constructor of this class
  public SongWriter(){
    this.initPitchToNoteDictionary();
  }
  
  // This initialises the pitchToNote dictionary,
  // which will be used by you to convert pitch numbers
  // to note letters
  public void initPitchToNoteDictionary(){
    pitchToNote  = new Hashtable<Integer, String>();
    pitchToNote.put(60, "C");
    pitchToNote.put(61, "C#");
    pitchToNote.put(62, "D");
    pitchToNote.put(63, "D#");
    pitchToNote.put(64, "E");
    pitchToNote.put(65, "F");
    pitchToNote.put(66, "F#");
    pitchToNote.put(67, "G");
    pitchToNote.put(68, "G#");
    pitchToNote.put(69, "A");
    pitchToNote.put(70, "A#");
    pitchToNote.put(71, "B");
  }
  
  // This method converts a single MidiNote to its notestring representation
  //I wrote the following method (MYCODE)
  public String noteToString(MidiNote note){
    String result = "";
    int octave= note.getOctave();
    result= "" + note.getDuration();
    /*First, I find the duration of the note/pause,
     * then I checked to see if the "note" was a pause in which case I ignored the octave*/
    if(note.isSilent()==true)
    {
      result= result+"P";
    }
    /*If it isn't a pause, I add/subtract the number of octaves the note is changed by and put it through
     * the hash table to find its note name*/
    else
    {
    result= result + (pitchToNote.get(note.getPitch()-12*note.getOctave()));
    }
    return result;
  }
  //I wrote the following method (MYCODE)
  public String trackToString(MidiTrack track){
    ArrayList<MidiNote> notes = track.getNotes();
    String result = "";
    int previous_octave = 0;
    MidiNote current_note;
    int diff=0;
    int i =0;
    while(i< notes.size())
    {
      /* First I check to see if the note in question is a pause, if so I ignore the octaves
       * if not, I set the variable 'octave' to the value of octave for the current note,
       * I then compare the number of octaves for this note to the previous note.
       * If the number of octaves are the same, I will just add the note to the string.
       * If there are not the same, in a loop, I will add the number of < or > required.*/
      if(notes.get(i).isSilent()==false)
      {
        int octave=notes.get(i).getOctave();
        diff= octave-previous_octave;
        previous_octave= octave;
        if(diff>0)
        {
          for(int j=0; j< diff; j++)
          {
            result= result + ">";
          }
          result= result + noteToString(notes.get(i));
          i++;
        }
        else if(diff<0)
        {
          for(int j=0; j< 0-diff; j++)
          {
            result= result + "<";
          }
          result= result + noteToString(notes.get(i));
          i++;
        }
        else
        {
          result= result + noteToString(notes.get(i));
          i++;
        }
      }
      else
      {
        result= result + noteToString(notes.get(i));
        i++;
      }
    }
    
    return result;
  }
  
  //I wrote the following method (MYCODE)
  public void writeToFile( Song s1, String file_path)throws IOException
  {
    /* Using a file write, I set name, bpm, soundbank and instrument to be the same. (With _reverse added to the name) 
     * I use trackToString to find the string representation of the track.*/
    FileWriter fw = new FileWriter(file_path);
    BufferedWriter bw = new BufferedWriter(fw);
    String name = s1.getName();
    String bpm= "" + s1.getBPM();
    String soundbank= s1.getSoundbank();
    String track = "";
    String instrument= "";
    String file = "name = " + name + "_reverse";
    bw.write(file);
    bw.newLine();
    file = "bpm = " + bpm;
    bw.write(file);
    bw.newLine();
    file = "soundbank = " + soundbank;
    bw.write(file);
    bw.newLine();
    ArrayList<MidiTrack> tracks = s1.getTracks();
    for(int i=0; i<tracks.size(); i++)
    {
      instrument= "" + tracks.get(i).getInstrumentId();
      file ="instrument = " + instrument;
      System.out.println(file);
      bw.write(file);
      bw.newLine();
      track = trackToString(tracks.get(i));
      file ="track = " + track;
      bw.write(file);
      bw.newLine();
    }
    bw.close();
    fw.close();
  }
  
  //I wrote the following method (MYCODE)
  public static void main( String[] args){
    SongWriter sw = new SongWriter();
    //MidiNote note = new MidiNote(60,3);
    String song_file_path = "./data/07.txt";
    Song mySong = new Song();
    try
    {
      mySong.loadFromFile(song_file_path);
    }
    catch (Exception e)
    {
      System.out.println("The file " + song_file_path + " was not found.");
    }
    System.out.println(mySong.getTracks().toString());
    mySong.revert();
    //String name= mySong.getName() + "_reverse";
    try
    {
      sw.writeToFile(mySong, song_file_path);
    }
    catch(Exception e)
    {
      System.out.println("The file" + song_file_path + "was not found.");
    }
    
    MusicInterpreter mi = new MusicInterpreter ();
    mi . loadSong ( mySong );
    mi . play ();
    mi . close ();
    
  }
}