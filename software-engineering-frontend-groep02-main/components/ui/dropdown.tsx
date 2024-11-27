import React, { useState, useEffect, useRef, ReactNode } from "react";

interface DropdownProps {
  label: React.ReactNode; // Changed from string to ReactNode
  children: ReactNode;
  className?: string;
  dropClassName?: string;
}

const Dropdown = ({
  label,
  children,
  className,
  dropClassName,
}: DropdownProps) => {
  const [isOpen, setIsOpen] = useState(false);
  const dropdownRef = useRef<HTMLDivElement>(null); // Ref for the dropdown container

  const toggleDropdown = () => setIsOpen(!isOpen);
  const closeDropdown = () => setIsOpen(false);

  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (
        dropdownRef.current &&
        !dropdownRef.current.contains(event.target as Node)
      ) {
        setIsOpen(false);
      }
    };

    if (isOpen) {
      document.addEventListener("mousedown", handleClickOutside);
    }

    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, [isOpen]);

  // Function to clone each child with the closeDropdown function as a prop
  const childrenWithProps = React.Children.map(children, (child) => {
    // Checking isValidElement is the safe way and avoids a TypeScript error too.
    if (React.isValidElement(child)) {
      return React.cloneElement(child, {
        onClick: closeDropdown,
      } as React.HTMLAttributes<HTMLElement>);
    }
    return child;
  });

  return (
    <div className="relative inline-block text-left" ref={dropdownRef}>
      <button onClick={toggleDropdown} className={`  ${className}`}>
        {label}
      </button>
      {isOpen && (
        <div
          className={`absolute mt-2 py-2 w-48 bg-white rounded-md shadow-xl z-20 border border-gray-300 ${dropClassName}`}>
          {childrenWithProps}
        </div>
      )}
    </div>
  );
};

export default Dropdown;
