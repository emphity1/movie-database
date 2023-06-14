function handleCommentDelete(event, reviewId) {
    event.preventDefault();
    
    // Effettua una richiesta al backend per eliminare il commento
    fetch(`/admin/delete-comment/${reviewId}`, {
        method: 'POST',
    })
    .then(response => {
        if (response.ok) {
            // Se la risposta del backend è OK, visualizza un pop-up con il messaggio di conferma
            swal("Eliminazione Commento", "Il commento è stato eliminato", "success");
        } else {
            // Se la risposta del backend non è OK, visualizza un pop-up con un messaggio di errore
            swal("Errore", "Si è verificato un errore durante l'eliminazione del commento", "error");
        }
    })
    .catch(error => {
        // In caso di errore nella richiesta al backend, visualizza un pop-up con un messaggio di errore
        swal("Errore", "Si è verificato un errore durante la richiesta al server", "error");
    });
}