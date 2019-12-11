package pl.wylezek.petclinic.petservice.exceptions.custom;

public class EmptyEntityListException extends RuntimeException {

    public EmptyEntityListException(Class classObject) {
        super(classObject.getSimpleName() + " was not found");
    }
}
