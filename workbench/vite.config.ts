import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import { contextApi } from './context-api.ts';
import { experimentApi } from './experiment-api.ts';

export default defineConfig({ plugins: [react(), contextApi(), experimentApi()], base: './', optimizeDeps: { include: ['exceljs'] } });
