package com.royaleleague.domain;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Tournament {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private String title;	// Free Solos, Trios, Quads
  private Date tournamentDate;	// Date of tournament Mon. Sept. 11, 2020 
  private int prize;	// 10 $, 100 $, 1000 $
  private String mode;	// Solos, Duos, Trios, Quads
  private Time duration;	// Duration of tournament 1H:30M, 2H:30M
  private Date startTime;	// Start time of tournament 10PM, 12AM etc.
  private int scoring;	// Best of "2","3" etc. games
  private int numOfTeam;	// Number of teams per division
  private int entryFee;	// Entry fee per player 5 $, 25 $ etc.
  private boolean active = true;	// Registration IF true than registration "open" IF false registration "closed"
  private int numOfRegisteredTeam;	// Registered teams on tournament
  private String imageType;

  // Transient - Won't be stored in Database
  @Transient
  private MultipartFile tournamentImage;

  @OneToMany(mappedBy = "tournament")
  @JsonIgnore
  private List<TournamentToCartItem> tournamentToCartItemList;

  public Long getId() {
	return id;
  }

  public void setId(Long id) {
	this.id = id;
  }

  public String getTitle() {
	return title;
  }

  public void setTitle(String title) {
	this.title = title;
  }

  public Date getTournamentDate() {
	return tournamentDate;
  }

  public void setTournamentDate(Date tournamentDate) {
	this.tournamentDate = tournamentDate;
  }

  public int getPrize() {
	return prize;
  }

  public void setPrize(int prize) {
	this.prize = prize;
  }

  public String getMode() {
	return mode;
  }

  public void setMode(String mode) {
	this.mode = mode;
  }

  public Time getDuration() {
	return duration;
  }

  public void setDuration(Time duration) {
	this.duration = duration;
  }

  public Date getStartTime() {
	return startTime;
  }

  public void setStartTime(Date startTime) {
	this.startTime = startTime;
  }

  public int getScoring() {
	return scoring;
  }

  public void setScoring(int scoring) {
	this.scoring = scoring;
  }

  public int getNumOfTeam() {
	return numOfTeam;
  }

  public void setNumOfTeam(int numOfTeam) {
	this.numOfTeam = numOfTeam;
  }

  public int getEntryFee() {
	return entryFee;
  }

  public void setEntryFee(int entryFee) {
	this.entryFee = entryFee;
  }

  public int getNumOfRegisteredTeam() {
	return numOfRegisteredTeam;
  }

  public void setNumOfRegisteredTeam(int numOfRegisteredTeam) {
	this.numOfRegisteredTeam = numOfRegisteredTeam;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public MultipartFile getBookImage() {
    return tournamentImage;
  }

  public void setBookImage(MultipartFile tournamentImage) {
    this.tournamentImage = tournamentImage;
  }

  public String getImageType() {
    return imageType;
  }

  public void setImageType(String imageType) {
    this.imageType = imageType;
  }

  public List<TournamentToCartItem> getTournamentToCartItemList() {
    return tournamentToCartItemList;
  }

  public void setTournamentToCartItemList(List<TournamentToCartItem> tournamentToCartItemList) {
    this.tournamentToCartItemList = tournamentToCartItemList;
  }

}
