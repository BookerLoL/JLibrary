package abstractfactory;

public class WildAnimalFactory implements AnimalFactory {

	@Override
	public Dog createDog() {
		return new WildDog();
	}

	@Override
	public Cat createCat() {
		return new WildCat();
	}

}
