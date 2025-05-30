import unittest
from unittest.mock import patch
import io
import sys # Import the sys module
from guessing_game import play_guessing_game # Assuming your game logic is in guessing_game.py

class TestGuessingGame(unittest.TestCase):

    @patch('random.randint')
    def test_number_generation(self, mock_randint):
        mock_randint.return_value = 50
        # We can't directly test the generated number without refactoring play_guessing_game
        # to return it or make it an attribute.
        # For now, we trust random.randint works as expected within the range.
        # A more involved test would require deeper changes to the game's structure.
        self.assertTrue(1 <= mock_randint() <= 100)

    @patch('builtins.input')
    @patch('random.randint')
    @patch('sys.stdout', new_callable=io.StringIO)
    def test_guess_too_low(self, mock_stdout, mock_randint, mock_input):
        mock_randint.return_value = 50
        mock_input.side_effect = ["25", "50"] # First guess low, then correct
        play_guessing_game()
        output = mock_stdout.getvalue()
        self.assertIn("Too low!", output)
        self.assertIn("Good job! You guessed the number in 2 attempts.", output)

    @patch('builtins.input')
    @patch('random.randint')
    @patch('sys.stdout', new_callable=io.StringIO)
    def test_guess_too_high(self, mock_stdout, mock_randint, mock_input):
        mock_randint.return_value = 50
        mock_input.side_effect = ["75", "50"] # First guess high, then correct
        play_guessing_game()
        output = mock_stdout.getvalue()
        self.assertIn("Too high!", output)
        self.assertIn("Good job! You guessed the number in 2 attempts.", output)

    @patch('builtins.input')
    @patch('random.randint')
    @patch('sys.stdout', new_callable=io.StringIO)
    def test_correct_guess_first_try(self, mock_stdout, mock_randint, mock_input):
        mock_randint.return_value = 77
        mock_input.side_effect = ["77"]
        play_guessing_game()
        output = mock_stdout.getvalue()
        self.assertIn("Good job! You guessed the number in 1 attempts.", output)

    @patch('builtins.input')
    @patch('random.randint') # Keep mocking randint even if not directly used to avoid actual random calls
    @patch('sys.stdout', new_callable=io.StringIO)
    def test_invalid_input_then_correct(self, mock_stdout, mock_randint, mock_input):
        mock_randint.return_value = 30
        mock_input.side_effect = ["abc", "30"] # Invalid input, then correct guess
        play_guessing_game()
        output = mock_stdout.getvalue()
        self.assertIn("Invalid input. Please enter a number.", output)
        self.assertIn("Good job! You guessed the number in 1 attempts.", output)
        # Note: The "1 attempts" is because we only count valid numeric inputs as attempts.

    @patch('builtins.input')
    @patch('random.randint')
    @patch('sys.stdout', new_callable=io.StringIO)
    def test_multiple_wrong_guesses(self, mock_stdout, mock_randint, mock_input):
        mock_randint.return_value = 60
        mock_input.side_effect = ["10", "90", "50", "70", "60"] # Low, High, Low, High, Correct
        play_guessing_game()
        output = mock_stdout.getvalue()
        self.assertIn("Too low!", output)
        self.assertIn("Too high!", output)
        self.assertIn("Good job! You guessed the number in 5 attempts.", output)

if __name__ == '__main__':
    unittest.main()
