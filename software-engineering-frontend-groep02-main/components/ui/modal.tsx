import React from "react";

const Modal = ({
  isOpen,
  onClose,
  onConfirm,
  control = "both",
  confirmColor = "blue",
  confirmText = "Confirm",
  abortColor = "red",
  abortText = "Abort",
  children,
  size = { width: "500px", height: "auto" }, // Default size added as a new prop
}: {
  isOpen: boolean;
  onClose: () => void;
  onConfirm?: () => void;
  control?: "both" | "confirm" | "abort" | "none";
  confirmColor?: string;
  confirmText?: string;
  abortColor?: string;
  abortText?: string;
  children: React.ReactNode;
  size?: { width: string; height: string }; // Specify the type for size
}) => {
  if (!isOpen) return null;

  const shouldShowConfirm = control === "both" || control === "confirm";
  const shouldShowAbort = control === "both" || control === "abort";

  // Dynamically set the style for the modal based on the `size` prop
  const modalStyle = {
    width: size.width,
    height: size.height,
  };

  return (
    <div
      onClick={onClose}
      className="fixed top-0 left-0 right-0 bottom-0 bg-black bg-opacity-50 flex items-center justify-center">
      <div
        onClick={(event) => {
          event.stopPropagation();
        }}
        className="bg-white p-8 rounded-md max-w-full"
        style={modalStyle}>
        {" "}
        {/* Apply the style here */}
        {children}
        {shouldShowAbort && (
          <button
            className={`rounded-md text-white px-2 py-1 focus:outline-none bg-${abortColor}-600 hover:bg-${abortColor}-800 focus:ring-2 focus:ring-${abortColor}-600 focus:ring-opacity-50`}
            onClick={onClose}>
            {abortText}
          </button>
        )}
        {shouldShowConfirm && onConfirm && (
          <button
            className={`rounded-md ml-2 text-white px-2 py-1 focus:outline-none bg-${confirmColor}-600 hover:bg-${confirmColor}-800 focus:ring-2 focus:ring-${confirmColor}-600 focus:ring-opacity-50`}
            onClick={onConfirm}>
            {confirmText}
          </button>
        )}
      </div>
    </div>
  );
};

export default Modal;
