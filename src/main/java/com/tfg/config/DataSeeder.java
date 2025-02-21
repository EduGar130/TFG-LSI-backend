package com.tfg.config;

import net.datafaker.Faker;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import com.tfg.entity.*;
import com.tfg.repository.*;

@Component
@RequiredArgsConstructor
public class DataSeeder {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;
    private final InventoryRepository inventoryRepository;
    private final TransactionRepository transactionRepository;
    private final AuditLogRepository auditLogRepository;
    private final PasswordEncoder passwordEncoder;

    Faker faker = new Faker(new Locale("es"));

    @PostConstruct
    @Transactional
    public void seedData() {
        seedRoles();
        seedCategories();
        seedWarehouses();
        seedUsers();
        seedProducts();
        seedInventory();
        seedTransactions();
        //seedAuditLogs();
    }

    private void seedUsers() {
        if (userRepository.count() == 0) {
            Role adminRole = roleRepository.findByName("admin").orElseThrow();
            Role marketingRole = roleRepository.findByName("marketing").orElseThrow();
            Role reponedorRole = roleRepository.findByName("reponedor").orElseThrow();
            Role managerRole = roleRepository.findByName("manager").orElseThrow();

            List<Warehouse> warehouses = warehouseRepository.findAll();

            //Creamos un admin
            User admin = new User("admin", passwordEncoder.encode("demo123"), faker.internet().emailAddress(), adminRole, null);
            userRepository.save(admin);

            //Creamos un usuario de marketing
            User marketing = new User("marketing", passwordEncoder.encode("demo123"), faker.internet().emailAddress(), marketingRole, null);
            userRepository.save(marketing);

            //Creamos un manager para cada almacén
            for (Warehouse warehouse : warehouses) {
                // Generamos un nombre de usuario único para el manager
                // Eliminamos acentos y espacios del nombre del almacén
                String warehouseName = warehouse.getName().replaceAll("[^a-zA-Z0-9]", "");
                User manager = new User("manager" + warehouseName, passwordEncoder.encode("demo123"), faker.internet().emailAddress(), managerRole, warehouse);
                userRepository.save(manager);
            }

            //Creamos entre 5 y 10 reponedores para cada almacén
            for (Warehouse warehouse : warehouses) {
                int numReponedores = faker.number().numberBetween(5, 10);
                for (int i = 0; i < numReponedores; i++) {
                    User reponedor = new User(faker.internet().username(), passwordEncoder.encode("demo123"), faker.internet().emailAddress(), reponedorRole, warehouse);
                    userRepository.save(reponedor);
                }
            }
        }
    }

    private void seedRoles() {
        if (roleRepository.count() == 0) {
            roleRepository.saveAll(List.of(
                    new Role("admin", "[\"full_access\"]", true),
                    new Role("marketing", "[\"view_reports\", \"view_stats\"]", true),
                    new Role("reponedor", "[\"manage_inventory\", \"view_alerts\" \"manage_transactions\"]", false),
                    new Role("manager", "[\"create_users\", \"manage_inventory\", \"view_alerts\", \"manage_transactions\"]", false)
            ));
        }
    }

    private void seedCategories() {
        if (categoryRepository.count() == 0) {
            IntStream.range(0, 6).forEach(i -> categoryRepository.save(
                    new Category(faker.commerce().department(), faker.lorem().sentence())
            ));
        }
    }

    private void seedWarehouses() {
        if (warehouseRepository.count() == 0) {
            IntStream.range(0, 5).forEach(i -> warehouseRepository.save(
                    new Warehouse("Almacén " + faker.address().city(), faker.address().fullAddress())
            ));
        }
    }


    private void seedProducts() {
        if (productRepository.count() == 0) {
            List<Category> categories = categoryRepository.findAll();
            IntStream.range(0, 200).forEach(i -> productRepository.save(
                    new Product(
                            faker.commerce().productName(),
                            faker.number().digits(8),
                            faker.lorem().sentence(),
                            new BigDecimal(faker.commerce().price(5.0, 200.0).replace(",", ".")),
                            faker.number().numberBetween(1, 50),
                            faker.options().nextElement(categories)
                    )
            ));
        }
    }

    private void seedInventory() {
        if (inventoryRepository.count() == 0) {
            List<Product> products = productRepository.findAll();
            List<Warehouse> warehouses = warehouseRepository.findAll();
            for (Product product : products) {
                for (Warehouse warehouse : warehouses) {
                    inventoryRepository.save(new Inventory(product, warehouse, faker.number().numberBetween(0, 500)));
                }
            }
        }
    }

    private void seedTransactions() {
        if (transactionRepository.count() == 0) {
            List<Product> products = productRepository.findAll();
            List<Warehouse> warehouses = warehouseRepository.findAll();
            Role reponedor = roleRepository.findByName("reponedor").orElseThrow();
            List<User> users = userRepository.findByRole(reponedor);

            IntStream.range(0, 10000).forEach(i -> {
                transactionRepository.save(new Transaction(
                        faker.options().nextElement(products),
                        faker.options().nextElement(warehouses),
                        faker.options().nextElement(users),
                        TransactionType.values()[faker.number().numberBetween(0, 3)],
                        faker.number().numberBetween(1, 10),
                        faker.lorem().sentence(),
                        faker.date().past(365, TimeUnit.DAYS).toLocalDateTime()
                ));
            });
        }
    }

    private void seedAuditLogs() {
        if (auditLogRepository.count() == 0) {
            List<User> users = userRepository.findAll();
            IntStream.range(0, 20).forEach(i -> auditLogRepository.save(
                    new AuditLog("INSERT", "products", i, users.get(i % users.size()), "{}")
            ));
        }
    }
}
