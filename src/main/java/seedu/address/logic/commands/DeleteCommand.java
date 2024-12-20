package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.client.Client;

/**
 * Deletes a client identified using it's displayed index from the address book.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the client identified by the index number used in the displayed client list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: '" + COMMAND_WORD + " 1'";

    public static final String MESSAGE_DELETE_CLIENT_SUCCESS = "Deleted Client: %1$s";
    public static final String MESSAGE_DELETE_CONFIRMATION = "This will permanently delete this client's contact. "
            + "Are you sure you want to execute this command? (y/n)";

    private static final boolean requiresConfirmation = true;

    private final Index targetIndex;

    public DeleteCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Client> lastShownList = model.getFilteredClientList();

        Client clientToDelete = lastShownList.get(targetIndex.getZeroBased());
        model.deleteClient(clientToDelete);
        String message = String.format(MESSAGE_DELETE_CLIENT_SUCCESS, Messages.format(clientToDelete));

        // For test compatibility, use the old constructor format
        return new CommandResult(message);
    }

    @Override
    public CommandResult execute(Model model, Boolean confirmationReceived) throws CommandException {
        if (confirmationReceived.equals(requiresConfirmation)) {
            List<Client> lastShownList = model.getFilteredClientList();
            Client clientToDelete = lastShownList.get(targetIndex.getZeroBased());
            model.deleteClient(clientToDelete);

            // Use the new constructor with deletion flags for the UI
            return new CommandResult(
                    String.format(MESSAGE_DELETE_CLIENT_SUCCESS, Messages.format(clientToDelete)),
                    false,
                    false,
                    false,
                    null,
                    true,
                    clientToDelete,
                    false
            );
        }

        if (targetIndex.getZeroBased() >= model.getFilteredClientList().size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_CLIENT_DISPLAYED_INDEX);
        }

        // For confirmation prompt
        return new CommandResult(
                MESSAGE_DELETE_CONFIRMATION,
                false,
                false,
                false,
                null,
                false,
                null,
                true
        );
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof DeleteCommand)) {
            return false;
        }

        DeleteCommand otherDeleteCommand = (DeleteCommand) other;
        return targetIndex.equals(otherDeleteCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
