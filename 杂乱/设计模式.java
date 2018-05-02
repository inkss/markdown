package com.szy.observer;

public interface ITalent {
  void newJob(String job);
}

package com.szy.observer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class Architect implements ITalent {
  
  private static final Logger LOG = LoggerFactory.getLogger(Architect.class);
  @Override
  public void newJob(String job) {
    LOG.info("Architect get new position {}", job);
  }
}

package com.szy.subject;
import java.util.ArrayList;
import java.util.Collection;
import com.szy.observer.ITalent;
public abstract class AbstractHR {
  protected Collection<ITalent> allTalents = new ArrayList<ITalent>();
  public abstract void publishJob(String job);
  public void addTalent(ITalent talent) {
    allTalents.add(talent);
  }
  public void removeTalent(ITalent talent) {
    allTalents.remove(talent);
  }
}

package com.szy.subject;
public class HeadHunter extends AbstractHR {
  @Override
  public void publishJob(String job) {
    allTalents.forEach(talent -> talent.newJob(job));
  }
}

package com.szy.client;
import com.szy.observer.Architect;
import com.szy.observer.ITalent;
import com.szy.observer.JuniorEngineer;
import com.szy.observer.SeniorEngineer;
import com.szy.subject.HeadHunter;
import com.szy.subject.AbstractHR;
public class Client1 {
  public static void main(String[] args) {
    ITalent juniorEngineer = new JuniorEngineer();
    ITalent seniorEngineer = new SeniorEngineer();
    ITalent architect = new Architect();
    AbstractHR subject = new HeadHunter();
    subject.addTalent(juniorEngineer);
    subject.addTalent(seniorEngineer);
    subject.addTalent(architect);
    subject.publishJob("Top 500 big data position");
  }
}


------------------------------------------------------------

package com.szy.organization;
import java.util.ArrayList;
import java.util.List;
public abstract class Organization {
  private List<Organization> childOrgs = new ArrayList<Organization>();
  private String name;
  public Organization(String name) {
    this.name = name;
  }
  public String getName() {
    return name;
  }
  public void addOrg(Organization org) {
    childOrgs.add(org);
  }
  public void removeOrg(Organization org) {
    childOrgs.remove(org);
  }
  public List<Organization> getAllOrgs() {
    return childOrgs;
  }
  public abstract void inform(String info);
  @Override
  public int hashCode(){
    return this.name.hashCode();
  }
  @Override
  public boolean equals(Object org){
    if(!(org instanceof Organization)) {
      return false;
    }
    return this.name.equals(((Organization) org).name);
  }
}

package com.szy.organization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class Department extends Organization{
  private static Logger LOGGER = LoggerFactory.getLogger(Department.class);
  public Department(String name) {
    super(name);
  }
  public void inform(String info){
    LOGGER.info("{}-{}", info, getName());
  }
}

package com.szy.organization;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class Company extends Organization{
  private static Logger LOGGER = LoggerFactory.getLogger(Company.class);
  public Company(String name) {
    super(name);
  }
  public void inform(String info){
    LOGGER.info("{}-{}", info, getName());
    List<Organization> allOrgs = getAllOrgs();
    allOrgs.forEach(org -> org.inform(info+"-"));
  }
}


-------------------------------------------------------------


package com.szy.brand;
import com.szy.transmission.Transmission;
public abstract class AbstractCar {
  protected Transmission gear;
  public abstract void run();
  public void setTransmission(Transmission gear) {
    this.gear = gear;
  }
}

package com.szy.brand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class BMWCar extends AbstractCar{
  private static final Logger LOG = LoggerFactory.getLogger(BMWCar.class);
  @Override
  public void run() {
    gear.gear();
    LOG.info("BMW is running");
  }
}

package com.szy.brand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class BenZCar extends AbstractCar{
  private static final Logger LOG = LoggerFactory.getLogger(BenZCar.class);
  @Override
  public void run() {
    gear.gear();
    LOG.info("BenZCar is running");
  }
}

package com.szy.brand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class LandRoverCar extends AbstractCar{
  private static final Logger LOG = LoggerFactory.getLogger(LandRoverCar.class);
  @Override
  public void run() {
    gear.gear();
    LOG.info("LandRoverCar is running");
  }
}

package com.szy.transmission;
public abstract class Transmission{
  public abstract void gear();
}

package com.szy.transmission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class Manual extends Transmission {
  private static final Logger LOG = LoggerFactory.getLogger(Manual.class);
  @Override
  public void gear() {
    LOG.info("Manual transmission");
  }
}

package com.szy.transmission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class Auto extends Transmission {
  private static final Logger LOG = LoggerFactory.getLogger(Auto.class);
  @Override
  public void gear() {
    LOG.info("Auto transmission");
  }
}

package com.szy.client;
import com.szy.brand.AbstractCar;
import com.szy.brand.BMWCar;
import com.szy.brand.BenZCar;
import com.szy.transmission.Auto;
import com.szy.transmission.Manual;
import com.szy.transmission.Transmission;
public class BridgeClient {
  public static void main(String[] args) {
    Transmission auto = new Auto();
    AbstractCar bmw = new BMWCar();
    bmw.setTransmission(auto);
    bmw.run();
    
    Transmission manual = new Manual();
    AbstractCar benz = new BenZCar();
    benz.setTransmission(manual);
    benz.run();
  }
}


------------------------------------------------

package com.szy.client;
import com.szy.product.BenzCar;
public class Driver1 {
  public static void main(String[] args) {
    BenzCar car = new BenzCar();
    car.drive();
  }
}

package com.szy.client;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.szy.product.BMWCar;
import com.szy.product.BenzCar;
import com.szy.product.Car;
import com.szy.product.LandRoverCar;
public class Driver2 {
  
  private static final Logger LOG = LoggerFactory.getLogger(Driver2.class);
  public static void main(String[] args) throws ConfigurationException {
    XMLConfiguration config = new XMLConfiguration("car.xml");
    String name = config.getString("driver2.name");
    Car car;
    switch (name) {
    case "Land Rover":
      car = new LandRoverCar();
      break;
    case "BMW":
      car = new BMWCar();
      break;
    case "Benz":
      car = new BenzCar();
      break;
    default:
      car = null;
      break;
    }
    LOG.info("Created car name is {}", name);
    car.drive();
  }
}

package com.szy.factory;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.szy.product.BMWCar;
import com.szy.product.BenzCar;
import com.szy.product.Car;
import com.szy.product.LandRoverCar;
public class CarFactory1 {
  
  private static final Logger LOG = LoggerFactory.getLogger(CarFactory1.class);
  public static Car newCar() {
    Car car = null;
    String name = null;
    try {
      XMLConfiguration config = new XMLConfiguration("car.xml");
      name = config.getString("factory1.name");
    } catch (ConfigurationException ex) {
      LOG.error("parse xml configuration file failed", ex);
    }
    switch (name) {
    case "Land Rover":
      car = new LandRoverCar();
      break;
    case "BMW":
      car = new BMWCar();
      break;
    case "Benz":
      car = new BenzCar();
      break;
    default:
      car = null;
      break;
    }
    LOG.info("Created car name is {}", name);
    return car;
  }
}

package com.szy.client;
import com.szy.factory.CarFactory1;
import com.szy.product.Car;
public class Driver3 {
  public static void main(String[] args) {
    Car car = CarFactory1.newCar();
    car.drive();
  }
}


---------------------------------------------------

package com.szy.strategy;
public interface Strategy {
  void strategy(String input);
}

package com.szy.strategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@com.szy.annotation.Strategy(name="StrategyA")
public class ConcreteStrategyA implements Strategy {
  private static final Logger LOG = LoggerFactory.getLogger(ConcreteStrategyB.class);
  @Override
  public void strategy(String input) {
    LOG.info("Strategy A for input : {}", input);
  }
}

package com.szy.strategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@com.szy.annotation.Strategy(name="StrategyB")
public class ConcreteStrategyB implements Strategy {
  private static final Logger LOG = LoggerFactory.getLogger(ConcreteStrategyB.class);
  @Override
  public void strategy(String input) {
    LOG.info("Strategy B for input : {}", input);
  }
}

package com.szy.context;
import com.szy.strategy.Strategy;
public class SimpleContext {
  private Strategy strategy;
  
  public SimpleContext(Strategy strategy) {
    this.strategy = strategy;
  }
  
  public void action(String input) {
    strategy.strategy(input);
  }
  
}

package com.szy.client;
import com.szy.context.SimpleContext;
import com.szy.strategy.ConcreteStrategyA;
import com.szy.strategy.Strategy;
public class SimpleClient {
  public static void main(String[] args) {
    Strategy strategy = new ConcreteStrategyA();
    SimpleContext context = new SimpleContext(strategy);
    context.action("Hellow, world");
  }
}

package com.szy.context;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.szy.strategy.Strategy;
public class SimpleFactoryContext {
  private static final Logger LOG = LoggerFactory.getLogger(SimpleFactoryContext.class);
  private static Map<String, Class> allStrategies;
  static {
    Reflections reflections = new Reflections("com.szy.strategy");
    Set<Class<?>> annotatedClasses =
        reflections.getTypesAnnotatedWith(com.szy.annotation.Strategy.class);
    allStrategies = new ConcurrentHashMap<String, Class>();
    for (Class<?> classObject : annotatedClasses) {
      com.szy.annotation.Strategy strategy = (com.szy.annotation.Strategy) classObject
          .getAnnotation(com.szy.annotation.Strategy.class);
      allStrategies.put(strategy.name(), classObject);
    }
    allStrategies = Collections.unmodifiableMap(allStrategies);
  }
  private Strategy strategy;
  public SimpleFactoryContext() {
    String name = null;
    try {
      XMLConfiguration config = new XMLConfiguration("strategy.xml");
      name = config.getString("strategy.name");
      LOG.info("strategy name is {}", name);
    } catch (ConfigurationException ex) {
      LOG.error("Parsing xml configuration file failed", ex);
    }
    if (allStrategies.containsKey(name)) {
      LOG.info("Created strategy name is {}", name);
      try {
        strategy = (Strategy) allStrategies.get(name).newInstance();
      } catch (InstantiationException | IllegalAccessException ex) {
        LOG.error("Instantiate Strategy failed", ex);
      }
    } else {
      LOG.error("Specified Strategy name {} does not exist", name);
    }
  }
  public void action(String input) {
    strategy.strategy(input);
  }
}

package com.szy.client;
import com.szy.context.SimpleFactoryContext;
public class SimpleFactoryClient {
  public static void main(String[] args) {
    SimpleFactoryContext context = new SimpleFactoryContext();
    context.action("Hellow, world");
  }
}


-------------------------------------------------------


package com.szy.flyweight;
public interface FlyWeight {
  void action(String externalState);
}

package com.szy.flyweight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class ConcreteFlyWeight implements FlyWeight {
  private static final Logger LOG = LoggerFactory.getLogger(ConcreteFlyWeight.class);
  private String name;
  public ConcreteFlyWeight(String name) {
    this.name = name;
  }
  @Override
  public void action(String externalState) {
    LOG.info("name = {}, outerState = {}", this.name, externalState);
  }
}

package com.szy.factory;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.szy.flyweight.ConcreteFlyWeight;
import com.szy.flyweight.FlyWeight;
public class FlyWeightFactory {
  private static final Logger LOG = LoggerFactory.getLogger(FlyWeightFactory.class);
  private static ConcurrentHashMap<String, FlyWeight> allFlyWeight = new ConcurrentHashMap<String, FlyWeight>();
  public static FlyWeight getFlyWeight(String name) {
    if (allFlyWeight.get(name) == null) {
      synchronized (allFlyWeight) {
        if (allFlyWeight.get(name) == null) {
          LOG.info("Instance of name = {} does not exist, creating it");
          FlyWeight flyWeight = new ConcreteFlyWeight(name);
          LOG.info("Instance of name = {} created");
          allFlyWeight.put(name, flyWeight);
        }
      }
    }
    return allFlyWeight.get(name);
  }
}