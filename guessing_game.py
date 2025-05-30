import random

def play_guessing_game():
    """Plays a number guessing game with the user."""
    secret_number = random.randint(1, 100)
    attempts = 0
    guess = None

    print("Welcome to the Number Guessing Game!")
    print("I'm thinking of a number between 1 and 100.")

    while guess != secret_number:
        try:
            guess = int(input("Take a guess: "))
            attempts += 1

            if guess < secret_number:
                print("Too low!")
            elif guess > secret_number:
                print("Too high!")
            else:
                print(f"Good job! You guessed the number in {attempts} attempts.")
        except ValueError:
            print("Invalid input. Please enter a number.")

if __name__ == "__main__":
    play_guessing_game()
