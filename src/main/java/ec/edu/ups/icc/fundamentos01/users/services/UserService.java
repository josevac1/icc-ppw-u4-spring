package ec.edu.ups.icc.fundamentos01.users.services;

import java.util.List;

import ec.edu.ups.icc.fundamentos01.products.dtos.ProductResponseDto;
import ec.edu.ups.icc.fundamentos01.users.dtos.CreateUserDto;
import ec.edu.ups.icc.fundamentos01.users.dtos.PartialUpdateUserDto;
import ec.edu.ups.icc.fundamentos01.users.dtos.PasswordUsers;
import ec.edu.ups.icc.fundamentos01.users.dtos.UpdateUserDto;
import ec.edu.ups.icc.fundamentos01.users.dtos.UserResponseDto;

public interface UserService {

    List<UserResponseDto> findAll();

    UserResponseDto findOne(int id);

    UserResponseDto create(CreateUserDto dto);

    UserResponseDto update(int id, UpdateUserDto dto);

    UserResponseDto partialUpdate(int id, PartialUpdateUserDto dto);

    void delete(int id);

    UserResponseDto passwordUser(int id, PasswordUsers dto);

    List<ProductResponseDto> getProductsByUserId(Long userId);

    /**
     * Obtiene los productos de un usuario aplicando filtros opcionales
     * a nivel de base de datos.
     * 
     * @param userId ID del usuario propietario de los productos
     * @param name Nombre del producto (búsqueda parcial, optional)
     * @param minPrice Precio mínimo (optional)
     * @param maxPrice Precio máximo (optional)
     * @param categoryId ID de la categoría (optional)
     * @return Lista de productos filtrados
     */
    List<ProductResponseDto> getProductsByUserIdWithFilters(
        Long userId,
        String name,
        Double minPrice,
        Double maxPrice,
        Long categoryId
    );
}

