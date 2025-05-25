package com.tfg.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfg.dto.CategoryDTO;
import com.tfg.dto.ProductDTO;
import lombok.Data;
import net.datafaker.Faker;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.tfg.entity.*;
import com.tfg.repository.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
@RequiredArgsConstructor
public class DataSeeder {

    @Value("${OPENAI_API_KEY}")
    private String apiKey;

    @Value("${tema_inventario}")
    private String temaInventario;

    @Value("${num_categorias}")
    private String numeroCategorias;

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;
    private final InventoryRepository inventoryRepository;
    private final InventoryAlertRepository inventoryAlertRepository;
    private final TransactionRepository transactionRepository;
    private final AuditLogRepository auditLogRepository;
    private final PasswordEncoder passwordEncoder;

    Faker faker = new Faker(new Locale("es"));

    @PostConstruct
    @Transactional
    public void seedData() {
        seedRoles();
        //seedCategories();
        seedWarehouses();
        seedCategoriesAndProducts();
        seedUsers();
        //seedProducts();
        seedInventory();
        seedTransactions();
        createAlerts();
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
                    new Role("reponedor", "[\"manage_inventory\", \"view_alerts\", \"manage_transactions\"]", false),
                    new Role("manager", "[\"create_users\", \"manage_inventory\", \"view_alerts\", \"manage_transactions\"]", false)
            ));
        }
    }

    private void seedCategoriesFallback() {
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


    private void seedProductsFallback() {
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
                        TransactionType.values()[faker.options().nextElement(List.of(0, 0, 1, 2))], // 50% de reposiciones, 50% de ventas o retiros
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

    private void seedCategoriesAndProducts() {
        if (categoryRepository.count() == 0 && productRepository.count() == 0) {
            try {
                int numeroCategorias = Integer.parseInt(this.numeroCategorias);
                List<String> nombresGenerados = new ArrayList<>();
                List<Category> categorias = new ArrayList<>();
                List<Product> productos = new ArrayList<>();

                for (int i = 1; i <= numeroCategorias; i++) {
                    String exclusiones = nombresGenerados.isEmpty()
                            ? ""
                            : String.join(", ", nombresGenerados) + ".";

                    String ejemplo = """
[
  {
    "nombre_categoria": "Lácteos",
    "descripcion": "Productos derivados de la leche.",
    "productos": [
      {
        "nombre": "Leche entera",
        "sku": "12345678",
        "descripcion": "Leche entera pasteurizada ideal para el desayuno.",
        "precio": 1.2
      }
    ]
  }
]
""";

                    String prompt = "Genera en español de españa un JSON con UNA sola categoría relacionada con " + temaInventario + "." +
                            " El nombre debe ser coherente con la temática y distinto a: " + temaInventario + ", " +
                            exclusiones + " Añade 10 productos para esa categoría, cada uno con: nombre, SKU (8 dígitos), descripción y precio." +
                            " Usa esta estructura exacta (no reutilices los datos):\n" + ejemplo;

                    String rawJson = callOpenAI(prompt);
                    String jsonContent = extractJsonContent(rawJson);

                    List<Category> nuevasCategorias = parseAndSaveCategoriesFromJson(jsonContent);
                    List<Product> nuevosProductos = parseAndSaveProductsFromJson(jsonContent, nuevasCategorias);

                    for (Category c : nuevasCategorias) {
                        if (nombresGenerados.contains(c.getName())) {
                            throw new RuntimeException("Nombre de categoría duplicado detectado en la iteración " + i);
                        }
                        nombresGenerados.add(c.getName());
                        categorias.add(c);
                    }
                    productos.addAll(nuevosProductos);
                }

                if (categorias.isEmpty() || productos.isEmpty()) {
                    throw new RuntimeException("No se generaron categorías o productos válidos");
                }

            } catch (Exception e) {
                System.out.println("Error al usar la API. Usando seeder clásico. Motivo: " + e.getMessage());
                seedCategoriesFallback();
                seedProductsFallback();
            }
        }
    }


    private String callOpenAI(String prompt) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        String body = "{"
                + "\"model\":\"gpt-3.5-turbo\","
                + "\"messages\":[{\"role\":\"user\",\"content\":\"" +  escapeJson(prompt) + "\"}]"
                + "}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }

    private String escapeJson(String text) {
        return text.replace("\"", "\\\"").replace("\n", "\\n");
    }

    private List<Category> parseAndSaveCategoriesFromJson(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<CategoriaGeneradaDTO> categoriasDTO = mapper.readValue(json, new TypeReference<>() {});

        List<Category> categorias = new ArrayList<>();
        for (CategoriaGeneradaDTO dto : categoriasDTO) {
            Category category = new Category(dto.getNombre(), dto.getDescripcion());
            categoryRepository.save(category);
            categorias.add(category);
        }
        return categorias;
    }

    private List<Product> parseAndSaveProductsFromJson(String json, List<Category> categoriasGuardadas) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<CategoriaGeneradaDTO> categoriasDTO = mapper.readValue(json, new TypeReference<>() {});

        Map<String, Category> nombreToCategoria = categoriasGuardadas.stream()
                .collect(Collectors.toMap(Category::getName, c -> c));

        List<Product> productos = new ArrayList<>();
        for (CategoriaGeneradaDTO dto : categoriasDTO) {
            Category categoria = nombreToCategoria.get(dto.getNombre());
            if (categoria == null) continue;

            for (ProductoGeneradoDTO p : dto.getProductos()) {
                Boolean existeSKU = true;
                while(existeSKU){
                    String sku = p.getSku();
                    if (productRepository.existsBySku(sku)) {
                        sku = faker.number().digits(8);
                        p.setSku(sku);
                    } else {
                        existeSKU = false;
                    }
                }
                Product producto = new Product(
                        p.getNombre(),
                        p.getSku(),
                        p.getDescripcion(),
                        p.getPrecio(),
                        faker.number().numberBetween(1, 50),
                        categoria
                );
                productRepository.save(producto);
                productos.add(producto);
            }
        }
        return productos;
    }

    @Data
    static class CategoriaGeneradaDTO {
        @JsonProperty("nombre_categoria")
        private String nombre;
        private String descripcion;
        private List<ProductoGeneradoDTO> productos;
    }

    @Data
    static class ProductoGeneradoDTO {
        private String nombre;
        private String sku;
        private String descripcion;
        private BigDecimal precio;
    }

    private String extractJsonContent(String responseBody) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(responseBody);
        return root.path("choices").get(0).path("message").path("content").asText();
    }

    private void createAlerts() {
        List<Inventory> inventories = inventoryRepository.findAll();
        for (Inventory inventory : inventories) {
            // Verifica si la cantidad de inventario es menor o igual al umbral de alerta y no existe una alerta previa
            if ((inventory.getQuantity() <= inventory.getProduct().getStockAlertThreshold()) &&
            inventoryAlertRepository.findByProductAndWarehouse(inventory.getProduct(), inventory.getWarehouse()).isEmpty()) {
                String message = String.format("El producto %s en: %s tiene un stock bajo: %d unidades. Stock mínimo: %d unidades.",
                        inventory.getProduct().getName(), inventory.getWarehouse().getName(), inventory.getQuantity(), inventory.getProduct().getStockAlertThreshold());

                InventoryAlert alert = new InventoryAlert(inventory.getProduct(), inventory.getWarehouse(), "Stock bajo", message);
                inventoryAlertRepository.save(alert);
            }
        }
    }
}
