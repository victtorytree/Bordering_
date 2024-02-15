// Класс для управления реестром домашних животных
public class Registry {
    // Список животных в реестре
    private List<Animal> animals;

    // Счетчик животных
    private int animalCounter;

    // Путь к файлу JSON
    private static final String JSON_FILE_PATH = "animal_data.json";

    // Формат даты
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    // Конструктор класса
    public Registry() {
        // Загрузка данных из файла JSON при создании экземпляра класса
        this.animals = loadFromJsonFile();
        this.animalCounter = animals.size();
    }

    // Метод для загрузки данных из файла JSON
    private List<Animal> loadFromJsonFile() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            File file = new File(JSON_FILE_PATH);
            if (file.exists()) {
                // Чтение данных из файла JSON с использованием Jackson
                return mapper.readValue(file, new TypeReference<List<Animal>>() {
                });
            }
        } catch (IOException e) {
            System.out.println("Can`t upload data from file.");
        }
        // Возвращаем пустой список в случае ошибки
        return new ArrayList<>();
    }

    // Метод для сохранения данных в файл JSON
    private void saveToJsonFile() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            // Запись данных в файл JSON с использованием Jackson
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(JSON_FILE_PATH), animals);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Метод для добавления нового животного в реестр и сохранения данных в файл JSON
    public void addAnimal(Animal animal) {
        animals.add(animal);
        animalCounter++;
        saveToJsonFile();
    }

    // Метод для вывода списка команд для заданного животного
    public void listCommands(Animal animal) {
        System.out.println("List of commands for " + animal.getName() + ":");
        for (String command : animal.getCommands()) {
            System.out.println(command);
        }
    }

    // Метод для обучения новой команды для заданного животного
    public void teachNewCommand(Animal animal, String newCommand) {
        animal.teachNewCommand(newCommand);
        System.out.println("Command '" + newCommand + "' studied by " + animal.getName());
    }

    // Метод для вывода списка животных, отсортированных по дате рождения
    public void listAnimalsByBirthDate() {
        System.out.println("List of animals, by birthdate:");
        animals.sort(Comparator.comparing(Animal::getBirthDate));
        for (Animal animal : animals) {
            System.out.println("Nickname: " + animal.getName() + ", Genus: " + animal.getGenus() + ", Date of Birth: "
                    + new SimpleDateFormat(DATE_FORMAT).format(animal.getBirthDate()));
        }
    }

    // Метод для вывода общего количества животных
    public void showTotalAnimalCount() {
        System.out.println("Total animals quantity: " + animalCounter);
    }

    // Метод для вывода меню и обработки выбора пользователя
    public void printMenu() {
        System.out.println("1 -> Add new animal");
        System.out.println("2 -> Animal`s list of commands");
        System.out.println("3 -> Teach new command");
        System.out.println("4 -> List of animals by birthdate");
        System.out.println("5 -> Total quantity of animals");
        System.out.println("0 -> Exit");
        System.out.print("Your choice: ");
    }

    // Метод для парсинга даты из строки
    private Date parseDate(String input) {
        try {
            return new SimpleDateFormat(DATE_FORMAT).parse(input);
        } catch (ParseException e) {
            System.out.println("Incorrect data format. Please enter birthdate YYYY-MM-DD");
            return new Date();
        }
    }

    // Метод для создания животного на основе ввода пользователя
    private Animal createAnimalFromUserInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter nickname: ");
        String name = scanner.nextLine();
        System.out.print("Enter type (Cat, Dog or Hamster: ");
        String type = scanner.nextLine();
        System.out.print("Enter birthdate (YYYY-MM-DD): ");
        Date birthDate = parseDate(scanner.nextLine());

        switch (type.toLowerCase()) {
            case "Dog":
                return new Dog(name, birthDate);
            case "Cat":
                return new Cat(name, birthDate);
            case "Hamster":
                return new Hamster(name, birthDate);
            default:
                System.out.println("Incorrect animal genus");
                return null;
        }
    }

    // Метод для запуска программы
    public void start() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            printMenu();
            choice = scanner.nextInt();
            scanner.nextLine(); // Считываем символ новой строки

            switch (choice) {
                case 1:
                    Animal newAnimal = createAnimalFromUserInput();
                    if (newAnimal != null) {
                        addAnimal(newAnimal);
                        System.out.println("Animal successfully added");
                    }
                    break;
                case 2:
                    System.out.print("Enter nickname: ");
                    String animalName = scanner.nextLine();
                    Animal specifiedAnimal = findAnimalByName(animalName);
                    if (specifiedAnimal != null) {
                        listCommands(specifiedAnimal);
                    } else {
                        System.out.println("Animal not found");
                    }
                    break;
                case 3:
                    System.out.print("Enter nickname: ");
                    String animalNameToTeach = scanner.nextLine();
                    Animal animalToTeach = findAnimalByName(animalNameToTeach);
                    if (animalToTeach != null) {
                        System.out.print("Enter command which you want to teach: ");
                        String newCommand = scanner.nextLine();
                        teachNewCommand(animalToTeach, newCommand);
                    } else {
                        System.out.println("Animal not found");
                    }
                    break;
                case 4:
                    listAnimalsByBirthDate();
                    break;
                case 5:
                    showTotalAnimalCount();
                    break;
                case 0:
                    System.out.println("Bye bye...");
                    break;
                default:
                    System.out.println("Incorrect choice. PLease repeat");
            }
        } while (choice != 0);
    }

    // Метод для поиска животного по имени
    private Animal findAnimalByName(String name) {
        for (Animal animal : animals) {
            if (animal.getName().equalsIgnoreCase(name)) {
                return animal;
            }
        }
        return null;
    }
}