import React, { ChangeEvent } from 'react';

type Props = {
    label: string;
    value: string | number;
    name: string;
    onInput: (event: ChangeEvent<HTMLInputElement>) => void;
    id: string;
    type?: string;
};

const InputField: React.FC<Props> = ({ label, value, name, onInput, id, type }) => {
    return (
        <div className="mt-4">
            <label htmlFor={id} className="block text-gray-700 text-sm font-bold mb-2">
                {label}:
            </label>
            <input
                type={type ? type : 'text'}
                value={value}
                name={name}
                onInput={onInput}
                id={id}
                className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
            />
        </div>
    );
};

export default InputField;
