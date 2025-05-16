package ms.inditex.adapters.output.persistence.exceptions;

import ms.inditex.exeptions.AppException;

public class EntityNotFoundException extends AppException {
    public EntityNotFoundException(Long id) {
        super("Entity with id " + id + " not found");
    }
}
