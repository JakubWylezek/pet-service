package pl.wylezek.petclinic.petservice.exceptions.custom;

public class EntityAlreadyExistException extends RuntimeException {

    public EntityAlreadyExistException(Class classObject) {
        super(classObject.getSimpleName() + " was not found");
    }
}
