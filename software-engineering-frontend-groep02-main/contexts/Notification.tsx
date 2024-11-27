import React, { ReactNode, createContext, useContext, useState } from "react";

interface ConfirmationActions {
  onConfirm: () => Promise<void>;
  onCancel: () => void;
}

interface NotificationContextType {
  requestConfirmation: (
    modalContent: ReactNode,
    onConfirm: () => Promise<void>,
    onCancel: () => void
  ) => void;
  confirmAction: () => void;
  cancelAction: () => void;
  isVisible: boolean;
  modalContent: ReactNode;
}

const NotificationContext = createContext<NotificationContextType>({
  requestConfirmation: () => {},
  confirmAction: () => {},
  cancelAction: () => {},
  isVisible: false,
  modalContent: null,
});

export const useNotification = () => useContext(NotificationContext);

export const NotificationProvider = ({
  children,
}: {
  children: React.ReactNode;
}) => {
  const [isVisible, setIsVisible] = useState(false);
  const [modalContent, setModalContent] = useState<ReactNode>(null);
  const [confirmationActions, setConfirmationActions] =
    useState<ConfirmationActions>({
      onConfirm: async () => {},
      onCancel: () => {},
    });

  const requestConfirmation = (
    content: ReactNode,
    onConfirm: () => Promise<void>,
    onCancel: () => void
  ) => {
    setModalContent(content);
    setConfirmationActions({ onConfirm, onCancel });
    setIsVisible(true);
  };

  const confirmAction = async () => {
    if (confirmationActions.onConfirm) {
      await confirmationActions.onConfirm();
    }
    setIsVisible(false);
  };

  const cancelAction = () => {
    if (confirmationActions.onCancel) {
      confirmationActions.onCancel();
    }
    setIsVisible(false);
  };

  return (
    <NotificationContext.Provider
      value={{
        requestConfirmation,
        confirmAction,
        cancelAction,
        isVisible,
        modalContent,
      }}>
      {children}
    </NotificationContext.Provider>
  );
};
