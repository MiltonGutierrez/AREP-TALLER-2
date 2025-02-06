const apiClient = (() => {

    const url = '/app/';

    const getNotes = async () => {
        const response = await fetch(url + 'note');
        return response.json();
    };

    const addNote = async (title, group, content ) => {
        
        const promise = await fetch(`${url}note?title=${encodeURIComponent(title)}&group=${encodeURIComponent(group)}&content=${encodeURIComponent(content)}`, {
            method: 'POST',
        });
        return promise;
    }

    return {
        getNotes,
        addNote
    }
})();