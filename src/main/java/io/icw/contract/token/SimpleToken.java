package io.icw.contract.token;

import io.icw.contract.sdk.Address;
import io.icw.contract.sdk.Contract;
import io.icw.contract.sdk.Msg;
import io.icw.contract.sdk.Utils;
import io.icw.contract.sdk.annotation.Required;
import io.icw.contract.sdk.annotation.View;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class SimpleToken
  implements Contract, Token
{
  private final String name;
  private final String symbol;
  private final int decimals;
  private BigInteger totalSupply = BigInteger.ZERO;
  private Map<Address, BigInteger> balances = new HashMap();
  private Map<Address, Map<Address, BigInteger>> allowed = new HashMap();
  private Address owner;
  
  @View
  public String name()
  {
    return this.name;
  }
  
  @View
  public String symbol()
  {
    return this.symbol;
  }
  
  @View
  public int decimals()
  {
    return this.decimals;
  }
  
  @View
  public BigInteger totalSupply()
  {
    return this.totalSupply;
  }
  
  public SimpleToken(@Required String name, @Required String symbol, @Required BigInteger initialAmount, @Required int decimals)
  {
    this.name = name;
    this.symbol = symbol;
    this.decimals = decimals;
    this.totalSupply = initialAmount.multiply(BigInteger.TEN.pow(decimals));
    this.balances.put(Msg.sender(), this.totalSupply);
    this.owner = Msg.sender();
    Utils.emit(new Token.TransferEvent(null, Msg.sender(), this.totalSupply));
  }
  
  @View
  public BigInteger allowance(@Required Address owner, @Required Address spender)
  {
    Map<Address, BigInteger> ownerAllowed = (Map)this.allowed.get(owner);
    if (ownerAllowed == null) {
      return BigInteger.ZERO;
    }
    BigInteger value = (BigInteger)ownerAllowed.get(spender);
    if (value == null) {
      value = BigInteger.ZERO;
    }
    return value;
  }
  
  public boolean transferFrom(@Required Address from, @Required Address to, @Required BigInteger value)
  {
    subtractAllowed(from, Msg.sender(), value);
    subtractBalance(from, value);
    addBalance(to, value);
    Utils.emit(new Token.TransferEvent(from, to, value));
    return true;
  }
  
  @View
  public BigInteger balanceOf(@Required Address owner)
  {
    Utils.require(owner != null);
    BigInteger balance = (BigInteger)this.balances.get(owner);
    if (balance == null) {
      balance = BigInteger.ZERO;
    }
    return balance;
  }
  
  public boolean transfer(@Required Address to, @Required BigInteger value)
  {
    subtractBalance(Msg.sender(), value);
    addBalance(to, value);
    Utils.emit(new Token.TransferEvent(Msg.sender(), to, value));
    return true;
  }
  
  public boolean approve(@Required Address spender, @Required BigInteger value)
  {
    setAllowed(Msg.sender(), spender, value);
    Utils.emit(new Token.ApprovalEvent(Msg.sender(), spender, value));
    return true;
  }
  
  public boolean increaseApproval(@Required Address spender, @Required BigInteger addedValue)
  {
    addAllowed(Msg.sender(), spender, addedValue);
    Utils.emit(new Token.ApprovalEvent(Msg.sender(), spender, allowance(Msg.sender(), spender)));
    return true;
  }
  
  public boolean decreaseApproval(@Required Address spender, @Required BigInteger subtractedValue)
  {
    check(subtractedValue);
    BigInteger oldValue = allowance(Msg.sender(), spender);
    if (subtractedValue.compareTo(oldValue) > 0) {
      setAllowed(Msg.sender(), spender, BigInteger.ZERO);
    } else {
      subtractAllowed(Msg.sender(), spender, subtractedValue);
    }
    Utils.emit(new Token.ApprovalEvent(Msg.sender(), spender, allowance(Msg.sender(), spender)));
    return true;
  }
  
  private void addAllowed(Address address1, Address address2, BigInteger value)
  {
    BigInteger allowance = allowance(address1, address2);
    check(allowance);
    check(value);
    setAllowed(address1, address2, allowance.add(value));
  }
  
  private void subtractAllowed(Address address1, Address address2, BigInteger value)
  {
    BigInteger allowance = allowance(address1, address2);
    check(allowance, value, "Insufficient approved token");
    setAllowed(address1, address2, allowance.subtract(value));
  }
  
  private void setAllowed(Address address1, Address address2, BigInteger value)
  {
    check(value);
    Map<Address, BigInteger> address1Allowed = (Map)this.allowed.get(address1);
    if (address1Allowed == null)
    {
      address1Allowed = new HashMap();
      this.allowed.put(address1, address1Allowed);
    }
    address1Allowed.put(address2, value);
  }
  
  private void addBalance(Address address, BigInteger value)
  {
    BigInteger balance = balanceOf(address);
    check(value, "The value must be greater than or equal to 0.");
    check(balance);
    this.balances.put(address, balance.add(value));
  }
  
  private void subtractBalance(Address address, BigInteger value)
  {
    BigInteger balance = balanceOf(address);
    check(balance, value, "Insufficient balance of token.");
    this.balances.put(address, balance.subtract(value));
  }
  
  private void check(BigInteger value)
  {
    Utils.require((value != null) && (value.compareTo(BigInteger.ZERO) >= 0));
  }
  
  private void check(BigInteger value1, BigInteger value2)
  {
    check(value1);
    check(value2);
    Utils.require(value1.compareTo(value2) >= 0);
  }
  
  private void check(BigInteger value, String msg)
  {
    Utils.require((value != null) && (value.compareTo(BigInteger.ZERO) >= 0), msg);
  }
  
  private void check(BigInteger value1, BigInteger value2, String msg)
  {
    check(value1);
    check(value2);
    Utils.require(value1.compareTo(value2) >= 0, msg);
  }
  
  public boolean increase(@Required BigInteger amount)
  {
    if (!Msg.sender().equals(this.owner)) {
      return false;
    }
    BigInteger value = amount.multiply(BigInteger.TEN.pow(this.decimals));
    this.totalSupply = this.totalSupply.add(value);
    addBalance(this.owner, value);
    Utils.emit(new Token.TransferEvent(null, this.owner, value));
    return true;
  }
}
