import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileReader;
import java.util.ArrayList;

/*Sacha Perry-Fagant
 *260571927*/

public class Song{
  String myName;
  int myBeatsPerMinute;
  String mySoundbank;
  ArrayList<MidiTrack> myTracks;
  
  // The constructor of this class
  public Song(){
    myTracks = new ArrayList<MidiTrack>();
    myBeatsPerMinute = 200;
    mySoundbank = "";
    myName = "Default_Name";
  }
  
  // GETTER METHODS
  
  public String getName(){
    return myName;
  }
  
  public String getSoundbank(){
    return mySoundbank;
  }
  
  public int getBPM(){
    return myBeatsPerMinute;
  }
  
  public ArrayList<MidiTrack> getTracks(){
    return myTracks;
  }
  
  //I wrote the following method. (MYCODE)
  public void loadFromFile(String file_path)throws IOException
  {
    /* Using a file reader, I load the file and separate each line by = then check to see whether the first 
     * value in the array is equal to name, bpm, ect and set the name (or bpm ect) to the second value of the array.
     * I assume that each instrument is immediately followed by the track. If there is no instrument, I set an 
     * arbitrary number for instrument.*/
    //System.out.println(file_path);
    FileReader fr = new FileReader(file_path);
    BufferedReader br = new BufferedReader(fr);
    String currentLine = br.readLine();
    int instrument=0;
    String track="";
    while(currentLine != null)
    {
      String[] split = currentLine.split("=");
      if(split[0].trim().equals("name"))
      {
        this.myName= split[1].trim();
      }
      else if(split[0].trim().equals("bpm"))
      {
        this.myBeatsPerMinute= Integer.parseInt(split[1].trim());
      }
      else if(split[0].trim().equals("soundbank"))
      {
        this.mySoundbank= split[1].trim();
      }        
      else if(split[0].trim().equals("instrument"))
      {
        instrument= Integer.parseInt(split[1].trim());
        MidiTrack song = new MidiTrack(instrument);
        currentLine = br.readLine();
        split = currentLine.split("=");
        track= split[1].trim();
        song.loadNoteString(track);
        this.myTracks.add(song);
      }
      else if(split[0].trim().equals("track"))
      {
        MidiTrack song = new MidiTrack(instrument);
        track= split[1].trim();
        song.loadNoteString(track);
        this.myTracks.add(song);
      }
      currentLine = br.readLine();
    }
    
    br.close();
    fr.close(); 
  }
  
  
  public void revert(){
    for (int i = 0; i<myTracks.size(); i++){
      myTracks.get(i).revert();
    }
  }
}