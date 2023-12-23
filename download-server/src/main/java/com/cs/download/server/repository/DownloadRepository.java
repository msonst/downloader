package com.cs.download.server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cs.download.server.entity.DownloadEntity;

public interface DownloadRepository extends JpaRepository<DownloadEntity, Long> {
  List<DownloadEntity> findByUrl(String url);
}