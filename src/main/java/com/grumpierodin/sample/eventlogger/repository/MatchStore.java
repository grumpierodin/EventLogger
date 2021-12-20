package com.grumpierodin.sample.eventlogger.repository;

import com.grumpierodin.sample.eventlogger.repository.data.Match;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchStore extends JpaRepository<Match, String>{
}
