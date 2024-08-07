# Motus Game

This is an Android project for a simple Motus game, developed using the MVI (Model-View-Intent) architecture. The app uses various modern Android technologies to provide a seamless user experience.

## Technologies Used

- **Jetpack Compose**: For building the UI.
- **MockK**: For unit testing.
- **Retrofit**: For fetching the words list from a remote source.
- **DataStore**: For storing the words list locally. A full database solution was deemed unnecessary due to the simplicity of the data (a list of strings).
- **Hilt**: For Dependency Injection.

## Project Structure

- **UI**: Built using Jetpack Compose, following the MVI architecture.
- **ViewModel**: Manages the UI state and handles business logic.
- **Repository**: Fetches data from remote and local sources.
- **Dependency Injection**: Managed using Hilt for better modularity and testability.
- **Tests**: Includes ViewModel tests and basic UI tests.

## Tests

### UI Tests

- **Note**: Not all UI states have been tested. The logic remains consistent across different values, making it easy to add more tests.
- **Example**: You can refer to the included tests to see how to add more.

### ViewModel Tests

- **Example Provided**: A basic example of how to test the ViewModel is included.
- **Extensibility**: More test cases can be added easily. Since the repository is mocked, any data can be injected to test various scenarios.
  
### Unimplemented Tests
- **Repository testing**
- **Other UI tests**
- **Other ViewModel tests**

## Limitations

- **Motus Game**: Not all cases are handled. For example:
  - Adding the same character multiple times and detecting which letter must be flagged as wrong and others as wrong position.
  - Checking if the user chose different words on subsequent retries without reusing correct letters.
- **Landscape Mode**: Not handled.
