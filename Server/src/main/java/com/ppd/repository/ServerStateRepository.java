package com.ppd.repository;

import com.ppd.domain.ServerState;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServerStateRepository extends JpaRepository<ServerState, Long> {
}
