import React from 'react';

type Props = {
    onClick: (event: React.FormEvent<HTMLFormElement>) => void;
    value: string;
};

const SubmitButton: React.FC<Props> = ({ onClick, value }) => {
    return (
        <input
            type="submit"
            value={value}
            className="w-full bg-red-500 hover:bg-red-700 text-white p-2 rounded-md cursor-pointer"
            onClick={(event) => onClick(event as any)}
        />
    );
};

export default SubmitButton;
