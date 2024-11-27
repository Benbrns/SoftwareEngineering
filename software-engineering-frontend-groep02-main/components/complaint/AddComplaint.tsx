import { addComplaint } from "@/services/ComplaintService";
import { StatusMessage } from "@/types";
import classNames from "classnames";
import { useSession } from "next-auth/react";
import { useRouter } from "next/router";
import React, { useState } from "react";

const AddComplaint: React.FC = () => {
  // Status messages
  const [statusMessages, setStatusMessages] = useState<StatusMessage[]>([]);

  // Get user session
  const { data: session, status } = useSession();

  // Form fields
  const [senderEmail, setSenderEmail] = useState("");
  const [receiverEmail, setReceiverEmail] = useState("");
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");

  // Error messages
  const [senderEmailError, setSenderEmailError] = useState("");
  const [receiverEmailError, setReceiverEmailError] = useState("");
  const [titleError, setTitleError] = useState("");
  const [descriptionError, setDescriptionError] = useState("");

  const router = useRouter();

  // Clear error messages
  const clearErrors = () => {
    setSenderEmailError("");
    setReceiverEmailError("");
    setTitleError("");
    setDescriptionError("");
    setStatusMessages([]);
  };

  // Form validation
  const validate = () => {
    let isValid = true;

    // if (!senderEmail || senderEmail.trim() === "") {
    //   setSenderEmailError("senderEmail is required");
    //   isValid = false;
    // }
    if (!receiverEmail || receiverEmail.trim() === "") {
      setReceiverEmailError("receiverEmail is required");
      isValid = false;
    }
    if (!title || title.trim() === "") {
      setTitleError("Title is required");
      isValid = false;
    }
    if (!description || description.trim() === "") {
      setDescriptionError("Description is required");
      isValid = false;
    }

    return isValid;
  };

  // Form submit handler
  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    // Prevent default form submission
    event.preventDefault();

    // Clear error messages
    clearErrors();

    // Validate form fields
    if (!validate()) {
      return;
    }

    // Check if user is authenticated
    if (session) {
      // Call addComplaint service
      const response = await addComplaint(
        {
          senderEmail: session?.user.email,
          receiverEmail,
          title,
          description,
        },
        session?.user.token
      );

      // Check if complaint was created successfully
      if (response.status === 200) {
        setStatusMessages([
          {
            message: "complaint created successfully, Redirecting...",
            type: "success",
          },
        ]);

        // Redirect to complaint page
        setTimeout(() => {
          router.push("/complaint"); // Redirect to complaint page
        }, 2000);
      } else {
        setStatusMessages([
          { message: "Failed to create complaint", type: "error" },
        ]);
      }
    }

    return;
  };

  // Render form
  return (
    <>
      <form onSubmit={handleSubmit} className="p-5 bg-white shadow rounded">
        {/* <div className="mb-4">
          <label htmlFor="senderEmailInput" className="block text-gray-700">
            SenderEmail
          </label>
          <input
            placeholder=""
            id="senderEmailInput"
            type="text"
            value={senderEmail}
            onChange={(event) => setSenderEmail(event.target.value)}
            className="w-full border border-gray-300 p-2 rounded-md"
          />
          {senderEmailError && (
            <div className="text-red-500">{senderEmailError}</div>
          )}
        </div> */}
        <div className="mb-4">
          <label htmlFor="receiverEmailInput" className="block text-gray-700">
            ReceiverEmail
          </label>
          <input
            placeholder=""
            id="receiverEmailInput"
            type="text"
            value={receiverEmail}
            onChange={(event) => setReceiverEmail(event.target.value)}
            className="w-full border border-gray-300 p-2 rounded-md"
          />
          {receiverEmailError && (
            <div className="text-red-500">{receiverEmailError}</div>
          )}
        </div>
        <div className="mb-4">
          <label htmlFor="titleInput" className="block text-gray-700">
            Title
          </label>
          <input
            placeholder=""
            id="titleInput"
            type="text"
            value={title}
            onChange={(event) => setTitle(event.target.value)}
            className="w-full border border-gray-300 p-2 rounded-md"
          />
          {titleError && <div className="text-red-500">{titleError}</div>}
        </div>
        <div className="mb-4">
          <label htmlFor="descriptionInput" className="block text-gray-700">
            Description
          </label>
          <textarea
            placeholder=""
            id="descriptionInput"
            value={description}
            onChange={(event) => setDescription(event.target.value)}
            className="w-full border border-gray-300 p-2 rounded-md"
          />
          {descriptionError && (
            <div className="text-red-500">{descriptionError}</div>
          )}
        </div>
        {statusMessages.map((statusMessage, index) => (
          <div
            key={index}
            className={classNames({
              "text-red-500": statusMessage.type === "error",
              "text-green-500": statusMessage.type === "success",
            })}
          >
            {statusMessage.message}
          </div>
        ))}
        <button type="submit" className="p-2 bg-blue-500 rounded-md text-white">
          Make Complaint
        </button>
      </form>
    </>
  );
};

export default AddComplaint;
