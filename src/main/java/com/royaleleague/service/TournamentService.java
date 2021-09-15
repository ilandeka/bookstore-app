package com.royaleleague.service;

import java.util.List;

import com.royaleleague.domain.Tournament;

public interface TournamentService {
	List<Tournament> findAll();

	Tournament findOne(Long id);
}
