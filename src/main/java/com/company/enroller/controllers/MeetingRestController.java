package com.company.enroller.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;
import com.company.enroller.persistence.ParticipantService;

@RestController
@RequestMapping("/meetings")
public class MeetingRestController {

	@Autowired
	ParticipantService participantService;

	@Autowired
	MeetingService meetingService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	// GET localhost:8080/meetings
	public ResponseEntity<?> getMeetings() {
		Collection<Meeting> meetings = meetingService.getAll();
		return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
	}

	// GET http://localhost:8080/meetings/2
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getMeetings(@PathVariable("id") long id) {
		Meeting meeting = meetingService.findById(id);
		if (meeting == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
	}

	// POST http://localhost:8080/meetings
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<?> organizeMeetings(@RequestBody Meeting meeting) {
		if (meetingService.findById(meeting.getId()) != null) {
			return new ResponseEntity<String>("Unable to organize meeting with id '" + meeting.getId(),
					HttpStatus.CONFLICT);
		}
		meetingService.add(meeting);
		return new ResponseEntity<Meeting>(HttpStatus.CREATED);
	}

	// DELETE http://localhost:8080/meetings/2
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteMeetings(@PathVariable("id") long id) {
		Meeting meeting = meetingService.findById(id);
		if (meeting == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		meetingService.delete(meeting);
		return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
	}

	// PUT http://localhost:8080/meetings/2
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> deleteMeeting(@PathVariable("id") long id, @RequestBody Meeting meeting) {
		Meeting foundMeetnig = meetingService.findById(id);
		if (foundMeetnig == null) {
			return new ResponseEntity<Meeting>(HttpStatus.NOT_FOUND);
		}

		meeting.setId(id);
		meetingService.update(meeting);

		return new ResponseEntity<Meeting>(foundMeetnig, HttpStatus.OK);
	}

	// GET http://localhost:8080/meetings/2/participants
	@RequestMapping(value = "/{id}/participants", method = RequestMethod.GET)
	public ResponseEntity<?> getListofParticipants(@PathVariable("id") long id) {
		Meeting meeting = meetingService.findById(id);
		if (meeting == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		Collection<Participant> participants = meeting.getParticipants();
		return new ResponseEntity<Collection<Participant>>(participants, HttpStatus.OK);
	}

	// POST http://localhost:8080/meetings/2/participants
	@RequestMapping(value = "/{id}/participants", method = RequestMethod.POST)
	public ResponseEntity<?> addParticipantToMeeting(@PathVariable("id") long id, @RequestBody Participant participant) {
		Meeting meeting = meetingService.findById(id);
		if (meeting == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		meeting.addParticipant(participant);
		return new ResponseEntity<Meeting>(HttpStatus.OK);
	}
	
	// DELETE http://localhost:8080/meetings/2/participants/user2
	@RequestMapping(value = "/{id}/participants/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteParticipantFromMeeting(@PathVariable("id") long id, @RequestBody Participant participant, @PathVariable("id") String login) {
		Meeting meeting = meetingService.findById(id);
		if (meeting == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		meeting.removeParticipant(participant);
		return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
	}
	

}
