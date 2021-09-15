package com.royaleleague.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.royaleleague.domain.Tournament;
import com.royaleleague.repository.TournamentRepository;
import com.royaleleague.service.TournamentService;

@Service
public class TournamentServiceImplementation implements TournamentService {

	@Autowired
	private TournamentRepository tournamentRepository;

	@Override
	public List<Tournament> findAll() {
		return (List<Tournament>) tournamentRepository.findAll();
	}

	@Override
	public Tournament findOne(Long id) {
		return tournamentRepository.findOne(id);
	}
}
