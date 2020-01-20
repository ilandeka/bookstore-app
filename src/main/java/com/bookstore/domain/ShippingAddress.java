package com.bookstore.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ShippingAddress {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private String shippingAddressStreet1;
  private String shippingAddressStreet2;
  private String shippingAddressCity;
  private String shippingAddressState;
  private String shippingAddressCountry;
  private String shippingAddressZipcode;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getshippingAddressStreet1() {
    return shippingAddressStreet1;
  }

  public void setshippingAddressStreet1(String shippingAddressStreet1) {
    this.shippingAddressStreet1 = shippingAddressStreet1;
  }

  public String getshippingAddressStreet2() {
    return shippingAddressStreet2;
  }

  public void setshippingAddressStreet2(String shippingAddressStreet2) {
    this.shippingAddressStreet2 = shippingAddressStreet2;
  }

  public String getshippingAddressCity() {
    return shippingAddressCity;
  }

  public void setshippingAddressCity(String shippingAddressCity) {
    this.shippingAddressCity = shippingAddressCity;
  }

  public String getshippingAddressState() {
    return shippingAddressState;
  }

  public void setshippingAddressState(String shippingAddressState) {
    this.shippingAddressState = shippingAddressState;
  }

  public String getshippingAddressCountry() {
    return shippingAddressCountry;
  }

  public void setshippingAddressCountry(String shippingAddressCountry) {
    this.shippingAddressCountry = shippingAddressCountry;
  }

  public String getshippingAddressZipcode() {
    return shippingAddressZipcode;
  }

  public void setshippingAddressZipcode(String shippingAddressZipcode) {
    this.shippingAddressZipcode = shippingAddressZipcode;
  }

}
