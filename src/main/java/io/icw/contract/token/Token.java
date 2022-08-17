package io.icw.contract.token;

import io.icw.contract.sdk.Address;
import io.icw.contract.sdk.Event;
import io.icw.contract.sdk.annotation.Required;
import io.icw.contract.sdk.annotation.View;
import java.math.BigInteger;

public abstract interface Token
{
  @View
  public abstract String name();
  
  @View
  public abstract String symbol();
  
  @View
  public abstract int decimals();
  
  @View
  public abstract BigInteger totalSupply();
  
  @View
  public abstract BigInteger balanceOf(@Required Address paramAddress);
  
  public abstract boolean transfer(@Required Address paramAddress, @Required BigInteger paramBigInteger);
  
  public abstract boolean transferFrom(@Required Address paramAddress1, @Required Address paramAddress2, @Required BigInteger paramBigInteger);
  
  public abstract boolean approve(@Required Address paramAddress, @Required BigInteger paramBigInteger);
  
  @View
  public abstract BigInteger allowance(@Required Address paramAddress1, @Required Address paramAddress2);
  
  public static class TransferEvent
    implements Event
  {
    private Address from;
    private Address to;
    private BigInteger value;
    
    public TransferEvent(Address from, @Required Address to, @Required BigInteger value)
    {
      this.from = from;
      this.to = to;
      this.value = value;
    }
    
    public Address getFrom()
    {
      return this.from;
    }
    
    public void setFrom(Address from)
    {
      this.from = from;
    }
    
    public Address getTo()
    {
      return this.to;
    }
    
    public void setTo(Address to)
    {
      this.to = to;
    }
    
    public BigInteger getValue()
    {
      return this.value;
    }
    
    public void setValue(BigInteger value)
    {
      this.value = value;
    }
    
    public boolean equals(Object o)
    {
      if (this == o) {
        return true;
      }
      if ((o == null) || (getClass() != o.getClass())) {
        return false;
      }
      TransferEvent that = (TransferEvent)o;
      if (this.from != null ? !this.from.equals(that.from) : that.from != null) {
        return false;
      }
      if (this.to != null ? !this.to.equals(that.to) : that.to != null) {
        return false;
      }
      return that.value == null ? true : this.value != null ? this.value.equals(that.value) : false;
    }
    
    public int hashCode()
    {
      int result = this.from != null ? this.from.hashCode() : 0;
      result = 31 * result + (this.to != null ? this.to.hashCode() : 0);
      result = 31 * result + (this.value != null ? this.value.hashCode() : 0);
      return result;
    }
    
    public String toString()
    {
      return "TransferEvent{from=" + this.from + ", to=" + this.to + ", value=" + this.value + '}';
    }
  }
  
  public static class ApprovalEvent
    implements Event
  {
    private Address owner;
    private Address spender;
    private BigInteger value;
    
    public ApprovalEvent(@Required Address owner, @Required Address spender, @Required BigInteger value)
    {
      this.owner = owner;
      this.spender = spender;
      this.value = value;
    }
    
    public Address getOwner()
    {
      return this.owner;
    }
    
    public void setOwner(Address owner)
    {
      this.owner = owner;
    }
    
    public Address getSpender()
    {
      return this.spender;
    }
    
    public void setSpender(Address spender)
    {
      this.spender = spender;
    }
    
    public BigInteger getValue()
    {
      return this.value;
    }
    
    public void setValue(BigInteger value)
    {
      this.value = value;
    }
    
    public boolean equals(Object o)
    {
      if (this == o) {
        return true;
      }
      if ((o == null) || (getClass() != o.getClass())) {
        return false;
      }
      ApprovalEvent that = (ApprovalEvent)o;
      if (this.owner != null ? !this.owner.equals(that.owner) : that.owner != null) {
        return false;
      }
      if (this.spender != null ? !this.spender.equals(that.spender) : that.spender != null) {
        return false;
      }
      return that.value == null ? true : this.value != null ? this.value.equals(that.value) : false;
    }
    
    public int hashCode()
    {
      int result = this.owner != null ? this.owner.hashCode() : 0;
      result = 31 * result + (this.spender != null ? this.spender.hashCode() : 0);
      result = 31 * result + (this.value != null ? this.value.hashCode() : 0);
      return result;
    }
    
    public String toString()
    {
      return "ApprovalEvent{owner=" + this.owner + ", spender=" + this.spender + ", value=" + this.value + '}';
    }
  }
}
