import React, { useEffect, useState } from 'react';
import api from '../services/api';
import SearchBar from './Searchbar';
import Select from './Select';
import Navbar from './Navbar';
import Modal from './Modal';
import EditBookModal from './EditBookModal';
import { toast } from 'react-toastify';

const BookList = () => {
  const [books, setBooks] = useState([]);
  const [filteredBooks, setFilteredBooks] = useState([]);
  const [categories, setCategories] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedCategory, setSelectedCategory] = useState('');
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const [bookToDelete, setBookToDelete] = useState(null);
  const [bookToEdit, setBookToEdit] = useState(null);

  useEffect(() => {
    Promise.all([
      api.get('/books'),
      api.get('/categories')
    ])
    .then(([booksResponse, categoriesResponse]) => {
      setBooks(booksResponse.data);
      setFilteredBooks(booksResponse.data);
      setCategories(categoriesResponse.data);
    })
    .catch(error => {
      console.error('Error fetching data:', error);
      toast.error('Failed to fetch data.');
    });
  }, []);

  const handleSearch = (value) => {
    setSearchTerm(value);
    filterBooks(value, selectedCategory);
  };

  const handleCategoryChange = (category) => {
    setSelectedCategory(category);
    filterBooks(searchTerm, category);
  };

  const filterBooks = (term, category) => {
    const filteredBooks = books.filter(book => {
      const bookName = book.name || '';
      const bookCategory = book.category || '';

      return bookName.toLowerCase().includes(term.toLowerCase()) &&
        (category === '' || bookCategory.toLowerCase() === category.toLowerCase());
    });
    setFilteredBooks(filteredBooks);
  };

  const handleDeleteBook = (id) => {
    api.delete(`/book/${id}`)
      .then(() => {
        const updatedBooks = books.filter(book => book.id !== id);
        setBooks(updatedBooks);
        setFilteredBooks(updatedBooks);
        toast.success('Book deleted successfully!');
      })
      .catch(error => {
        console.error('Failed to delete the book:', error);
        toast.error('Failed to delete the book.');
      });
    closeModal();
  };

  const handleSaveBook = (updatedBook) => {
    if (!updatedBook.id) {
      toast.error('Book ID is missing. Update failed.');
      return;
    }

    api.put(`/book/${updatedBook.id}`, updatedBook)
      .then(response => {
        const updatedBookFromResponse = response.data;
        const updatedBooks = books.map(book =>
          book.id === updatedBookFromResponse.id ? updatedBookFromResponse : book
        );
        setBooks(updatedBooks);
        setFilteredBooks(updatedBooks);
        toast.success('Book updated successfully!');
        closeEditModal();
      })
      .catch(error => {
        console.error('Failed to update the book:', error);
        toast.error('Failed to update the book.');
      });
  };

  const openModal = (book) => {
    setIsModalOpen(true);
    setBookToDelete(book);
  };

  const closeModal = () => {
    setIsModalOpen(false);
    setBookToDelete(null);
  };

  const openEditModal = (book) => {
    setIsEditModalOpen(true);
    setBookToEdit(book);
  };

  const closeEditModal = () => {
    setIsEditModalOpen(false);
    setBookToEdit(null);
  };

  return (
    <div className="">
      <Navbar />
      <h1 className='text-5xl font-bold text-center m-12 mb-24 text-[rgb(0,223,154)]'>Book List</h1>
      <div className='flex m-12'>
        <SearchBar onSearch={handleSearch} />
        <Select 
          value={selectedCategory} 
          onChange={handleCategoryChange} 
          categories={categories} 
        />
      </div>
      <div className="overflow-x-auto">
        <table className="min-w-full divide-y divide-gray-200">
          <thead className="bg-gray-50 dark:bg-gray-700">
            <tr>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-100 uppercase tracking-wider">ISBN</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-100 uppercase tracking-wider">Name</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-100 uppercase tracking-wider">Author</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-100 uppercase tracking-wider">Page Count</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-100 uppercase tracking-wider">Category</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-100 uppercase tracking-wider">Description</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-100 uppercase tracking-wider"></th>
            </tr>
          </thead>
          <tbody className="bg-white divide-y divide-gray-200 dark:bg-gray-800">
            {filteredBooks.map(book => (
              <tr key={book.id}>
                <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-100">{book.isbn}</td>
                <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-100">{book.name}</td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-100">{book.author}</td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-100">{book.pagesCount}</td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-100">{book.category}</td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-100">{book.description}</td>
                <td>
                  <button 
                    className="bg-red-500 hover:bg-red-700 text-white font-bold py-0.5 px-2 rounded mr-2" 
                    onClick={() => openModal(book)}
                  >
                    Delete
                  </button>
                  <button 
                    className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-0.5 px-2 rounded" 
                    onClick={() => openEditModal(book)}
                  >
                    Edit
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      <Modal 
        isOpen={isModalOpen} 
        title="Confirm Deletion" 
        message={`Are you sure you want to delete the book titled "${bookToDelete?.name}"?`} 
        onConfirm={() => handleDeleteBook(bookToDelete.id)} 
        onCancel={closeModal} 
      />
      <EditBookModal 
        isOpen={isEditModalOpen}
        book={bookToEdit}
        onSave={handleSaveBook}
        onCancel={closeEditModal}
      />
    </div>
  );
};

export default BookList;
