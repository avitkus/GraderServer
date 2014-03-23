package server.com.gradingProgram;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Andrew Vitkus
 *
 */
public class NoteData implements INoteData {
	private final HashMap<String, HashMap<String, List<String>>> noteData;
	
	private String currentSection;
	private String currentPart;
	
	public NoteData() {
		noteData = new HashMap<>(5);
	}
	
	@Override
	public void addSection(String section) {
		noteData.put(section, new HashMap<>(5));
		currentSection = section;
	}

	@Override
	public void addPart(String part) {
		noteData.get(currentSection).put(part, new ArrayList<>(5));
		currentPart = part;
	}

	@Override
	public void addNote(String note) {
		noteData.get(currentSection).get(currentPart).add(note);
	}

	@Override
	public String[] getSections() {
		return noteData.keySet().toArray(new String[noteData.size()]);
	}

	@Override
	public String[] getPartsForSection(String section) {
		HashMap<String, List<String>> parts = noteData.get(section);
		return parts.keySet().toArray(new String[parts.size()]);
	}

	@Override
	public String[] getNotesForPart(String section, String part) {
		ArrayList<String> notes = (ArrayList<String>) noteData.get(section).get(part);
		return notes.toArray(new String[notes.size()]);
	}

        @Override
	public boolean isEmpty() {
		return noteData.isEmpty();
	}
}
