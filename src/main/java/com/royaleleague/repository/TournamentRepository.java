package com.royaleleague.repository;

import org.springframework.data.repository.CrudRepository;

import com.royaleleague.domain.Tournament;

public interface TournamentRepository extends CrudRepository<Tournament, Long> {
	
}
