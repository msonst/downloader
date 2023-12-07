package com.cs.download.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.download.entity.DownloadEntity;
import com.cs.download.repository.DownloadRepository;

@Service
public class DownloadEntityService {

  @Autowired
  private DownloadRepository mDownloadRepository;

  public List<DownloadEntity> getAllEntities() {
    return mDownloadRepository.findAll();
  }

  public DownloadEntity getEntityById(Long id) {
    return mDownloadRepository.findById(id).orElse(null);
  }

  public Long saveEntity(DownloadEntity entity) {
    return mDownloadRepository.save(entity).getId();
  }

  public void deleteEntity(Long id) {
    mDownloadRepository.deleteById(id);
  }

  public List<DownloadEntity> findByUrl(String url) {
    return mDownloadRepository.findByUrl(url);
  }
}
